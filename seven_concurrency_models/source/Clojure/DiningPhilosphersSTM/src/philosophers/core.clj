(ns philosophers.core)

; START:philosophers
(def philosophers (into [] (repeatedly 5 #(ref :thinking))))
; END:philosophers

; START:claim-chopsticks
(defn claim-chopsticks [philosopher left right]
  (dosync
    (when (and (= (ensure left) :thinking) (= (ensure right) :thinking))
      (ref-set philosopher :eating))))
; END:claim-chopsticks

; START:release-chopsticks
(defn release-chopsticks [philosopher]
  (dosync (ref-set philosopher :thinking)))
; END:release-chopsticks

; START:philosopher-thread
(defn think []
  (Thread/sleep (rand 1000)))

(defn eat []
  (Thread/sleep (rand 1000)))

(defn philosopher-thread [n]
  (Thread.
    #(let [philosopher (philosophers n)
           left (philosophers (mod (- n 1) 5))
           right (philosophers (mod (+ n 1) 5))]
      (while true ; <label id="code.infinite-loop"/>
        (think)
        (when (claim-chopsticks philosopher left right) ; <label id="code.claim-chopsticks"/>
          (eat)
          (release-chopsticks philosopher)))))) ; <label id="code.release-chopsticks"/>

(defn -main [& args]
  (let [threads (map philosopher-thread (range 5))]
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))
; END:philosopher-thread
