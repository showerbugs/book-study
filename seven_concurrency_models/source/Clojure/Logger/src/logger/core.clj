(ns logger.core)

(defn now []
  (System/currentTimeMillis))

; START:logger
(def log-entries (agent []))

(defn log [entry]
  (send log-entries conj [(now) entry]))
; END:logger
