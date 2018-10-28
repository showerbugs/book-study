(ns sieve.core
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [clojure.edn :as edn]))

(defn factor? [x y]
  (zero? (mod y x)))

; START:getprimes
(defn get-primes []
  (let [primes (chan)
; START_HIGHLIGHT
        numbers (to-chan (iterate inc 2))]
; END_HIGHLIGHT
    (go-loop [ch numbers]
      (when-let [prime (<! ch)]
        (>! primes prime)
        (recur (remove< (partial factor? prime) ch)))
      (close! primes))
    primes))
; END:getprimes

; START:main
(defn -main [seconds]
  (let [primes (get-primes)
; START_HIGHLIGHT
        limit (timeout (* (edn/read-string seconds) 1000))]
; END_HIGHLIGHT
    (loop []
; START_HIGHLIGHT
      (alt!! :priority true
        limit nil
        primes ([prime] (println prime) (recur))))))
; END_HIGHLIGHT
; END:main
