(ns philosophers.core)

; START:philosophers
(def philosophers (atom (into [] (repeat 5 :thinking))))
; END:philosophers

; START:claim-chopsticks
(defn claim-chopsticks! [philosopher left right]
  (swap! philosophers
    (fn [ps] 
      (if (and (= (ps left) :thinking) (= (ps right) :thinking))
        (assoc ps philosopher :eating)
        ps)))
  (= (@philosophers philosopher) :eating))
; END:claim-chopsticks

; START:release-chopsticks
(defn release-chopsticks! [philosopher]
  (swap! philosophers assoc philosopher :thinking))
; END:release-chopsticks

(defn think []
  (Thread/sleep (rand 1000)))

(defn eat []
  (Thread/sleep (rand 1000)))

; START:philosopher-thread
(defn philosopher-thread [philosopher]
  (Thread.
; START_HIGHLIGHT
    #(let [left (mod (- philosopher 1) 5)
           right (mod (+ philosopher 1) 5)]
; END_HIGHLIGHT
      (while true
        (think)
        (when (claim-chopsticks! philosopher left right)
          (eat)
          (release-chopsticks! philosopher))))))
; END:philosopher-thread

(defn -main [& args]
  (let [threads (map philosopher-thread (range 5))]
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))