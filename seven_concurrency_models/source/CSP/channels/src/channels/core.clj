; START:ns
(ns channels.core
  (:require [clojure.core.async :as async :refer :all
              :exclude [map into reduce merge partition partition-by take]]))
; END:ns

; START:readall
(defn readall!! [ch]
  (loop [coll []]
    (if-let [x (<!! ch)]
      (recur (conj coll x))
      coll)))
; END:readall

; START:writeall
(defn writeall!! [ch coll]
  (doseq [x coll]
    (>!! ch x))
  (close! ch))
; END:writeall

; START:goadd
(defn go-add [x y]
  (<!! (nth (iterate #(go (inc (<! %))) (go x)) y)))
; END:goadd

; START:mapchan
(defn map-chan [f from]
  (let [to (chan)]
    (go-loop []
      (when-let [x (<! from)]
        (>! to (f x))
        (recur))
      (close! to))
    to))
; END:mapchan
