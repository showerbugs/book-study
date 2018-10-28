; START:parallel-sum
(ns sum.core
  (:require [clojure.core.reducers :as r]))

; END:parallel-sum
; START:recursive-sum
(defn recursive-sum [numbers]
  (if (empty? numbers)
    0
    (+ (first numbers) (recursive-sum (rest numbers)))))
; END:recursive-sum

; START:reduce-sum
(defn reduce-sum [numbers]
  (reduce (fn [acc x] (+ acc x)) 0 numbers))
; END:reduce-sum

; START:sum
(defn sum [numbers]
  (reduce + numbers))
; END:sum

; START:apply-sum
(defn apply-sum [numbers]
  (apply + numbers))
; END:apply-sum

; START:parallel-sum
(defn parallel-sum [numbers]
  (r/fold + numbers))
; END:parallel-sum
