(ns wordcount.core
  (:require [wordcount.pages :refer :all]
            [wordcount.words :refer :all]))

; START:count-words-sequential
(defn count-words-sequential [pages]
  (frequencies (mapcat get-words pages)))
; END:count-words-sequential

; START:count-words-parallel
(defn count-words-parallel [pages]
  (reduce (partial merge-with +)
    (pmap #(frequencies (get-words %)) pages)))
; END:count-words-parallel

; START:count-words
(defn count-words [pages]
  (reduce (partial merge-with +)
    (pmap count-words-sequential (partition-all 100 pages))))
; END:count-words

(defn -main [& args]
  (time (count-words (take 100000 (get-pages "enwiki.xml"))))
  (shutdown-agents))
