(ns philosophers.util)

; START:swap-when
(defn swap-when!
  "If (pred current-value-of-atom) is true, atomically swaps the value
  of the atom to become (apply f current-value-of-atom args). Note that
  both pred and f may be called multiple times and thus should be free
  of side effects. Returns the value that was swapped in if the
  predicate was true, nil otherwise."
  [a pred f & args]
  (loop [] ; <label id="code.loop"/>
    (let [old @a]
      (if (pred old)
        (let [new (apply f old args)] ; <label id="code.apply"/>
          (if (compare-and-set! a old new) ; <label id="code.compare-and-set"/>
            new
            (recur))) ; <label id="code.recur"/>
        nil))))
; END:swap-when