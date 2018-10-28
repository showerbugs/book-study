(ns reducers.parallel-frequencies
  (:require [clojure.core.reducers :as r]))

; START:parallel-frequencies
(defn parallel-frequencies [coll]
  (r/fold 
    (partial merge-with +)
    (fn [counts x] (assoc counts x (inc (get counts x 0))))
    coll))
; END:parallel-frequencies
