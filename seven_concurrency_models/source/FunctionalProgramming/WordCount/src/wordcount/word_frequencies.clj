(ns wordcount.word-frequencies)

; START:word-frequencies
(defn word-frequencies [words]
  (reduce 
    ; START:reducing-fn
    (fn [counts word] (assoc counts word (inc (get counts word 0))))
    ; END:reducing-fn
    {} words))
; END:word-frequencies
