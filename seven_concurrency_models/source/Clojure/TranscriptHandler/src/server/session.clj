(ns server.session
  (:require [schejulure.core     :refer [schedule]]
            [flatland.useful.map :refer [remove-vals]]))

; START:session-id
(def last-session-id (atom 0))
(defn next-session-id []
  (swap! last-session-id inc))
; END:session-id

; START:sessions
(def sessions (atom {}))

; START_HIGHLIGHT
(defn now []
  (System/currentTimeMillis))
; END_HIGHLIGHT

(defn new-session [initial]
  (let [session-id (next-session-id)
; START_HIGHLIGHT
        session (assoc initial :last-referenced (atom (now)))]
; END_HIGHLIGHT
    (swap! sessions assoc session-id session)
    session-id))

(defn get-session [id]
  (let [session (@sessions id)]
; START_HIGHLIGHT
    (reset! (:last-referenced session) (now))
; END_HIGHLIGHT
    session))
; END:sessions

; START:session-sweep
(defn session-expiry-time []
  (- (now) (* 10 60 1000)))
(defn expired? [session]
  (< @(:last-referenced session) (session-expiry-time)))

(defn sweep-sessions []
  (swap! sessions #(remove-vals % expired?)))
(def session-sweeper
  (schedule {:min (range 0 60 5)} sweep-sessions))
; END:session-sweep
