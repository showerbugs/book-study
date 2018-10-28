(ns animation.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.graphics :as graphics]
            [goog.events :as events]
            [cljs.core.async :refer [<! chan put! timeout]]))

; START:getevents
(defn get-events [elem event-type]
  (let [ch (chan)]
    (events/listen elem event-type
      #(put! ch %))
    ch))
; END:getevents

; START:shrinkingcircle
(def stroke (graphics/Stroke. 1 "#ff0000"))

(defn shrinking-circle [graphics x y]
  (go
    (let [circle (.drawCircle graphics x y 100 stroke nil)] ; <label id="code.drawcircle"/>
      (loop [r 100]
        (<! (timeout 25)) ; <label id="code.drawloop"/>
        (.setRadius circle r r)
        (when (> r 0)
          (recur (dec r))))
      (.dispose circle)))) ; <label id="code.dispose"/>
; END:shrinkingcircle

; START:creategraphics
(defn create-graphics [elem]
  (doto (graphics/createGraphics "100%" "100%")
    (.render elem)))
; END:creategraphics

; START:start
(defn start []
  (let [canvas (dom/getElement "canvas")
        graphics (create-graphics canvas)
        clicks (get-events canvas "click")]
    (go (while true
          (let [click (<! clicks)
                x (.-offsetX click)
                y (.-offsetY click)]
            (shrinking-circle graphics x y))))))

(set! (.-onload js/window) start)
; END:start
