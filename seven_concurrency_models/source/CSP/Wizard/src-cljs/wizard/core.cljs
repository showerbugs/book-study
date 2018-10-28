(ns wizard.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :refer [chan put!]]))

; START:utils
(defn show [elem]
  (set! (.. elem -style -display) "block"))

(defn hide [elem]
  (set! (.. elem -style -display) "none"))

(defn set-value [elem value]
  (set! (.-value elem) value))
; END:utils

(defn get-events [elem event-type]
  (let [ch (chan)]
    (events/listen elem event-type
      #(put! ch %))
    ch))

; START:start
(defn start []
  (go
    (let [wizard (dom/getElement "wizard")
          step1 (dom/getElement "step1")
          step2 (dom/getElement "step2")
          step3 (dom/getElement "step3")
          next-button (dom/getElement "next")
          next-clicks (get-events next-button "click")] ; <label id="code.nextclicks"/>
      (show step1)
      (<! next-clicks) ; <label id="code.waitforclick"/>
      (hide step1)
      (show step2)
      (<! next-clicks)
      (set-value next-button "Finish")
      (hide step2)
      (show step3)
      (<! next-clicks)
      (.submit wizard)))) ; <label id="code.submit"/>

(set! (.-onload js/window) start)
; END:start
