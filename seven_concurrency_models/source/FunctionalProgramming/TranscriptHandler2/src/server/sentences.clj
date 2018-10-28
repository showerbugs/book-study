(ns server.sentences
  (:require [clojure.string :refer [trim]]))

; START:sentence-split
(defn sentence-split [text]
  (map trim (re-seq #"[^\.!\?:;]+[\.!\?:;]*" text)))
; END:sentence-split

; START:is-sentence
(defn is-sentence? [text]
  (re-matches #"^.*[\.!\?:;]$" text))
; END:is-sentence

; START:strings-to-sentences
(defn sentence-join [x y]
  (if (is-sentence? x) y (str x " " y)))

(defn strings->sentences [strings]
  (filter is-sentence? 
    (reductions sentence-join 
      (mapcat sentence-split strings))))
; END:strings-to-sentences
