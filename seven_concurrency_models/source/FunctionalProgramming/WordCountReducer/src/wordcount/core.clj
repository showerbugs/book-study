(ns wordcount.core
  (:require [wordcount.pages :refer :all]
            [wordcount.words :refer :all]
            [clojure.core.reducers :as r]
            [foldable-seq.core :refer [foldable-seq]]))

; START:frequencies-parallel
(defn frequencies-parallel [words]
  (r/fold (partial merge-with +)
          (fn [counts word] (assoc counts word (inc (get counts word 0))))
          words))
; END:frequencies-parallel

; START:count-words
(defn count-words [pages]
  (frequencies-parallel (r/mapcat get-words (foldable-seq pages))))
; END:count-words

(defn -main [& args]
  (count-words (get-pages 10000 "enwiki.xml"))
  nil)
