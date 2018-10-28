(ns reducers.core
  (:require [clojure.core.protocols :refer [CollReduce coll-reduce]]
            [clojure.core.reducers :refer [CollFold coll-fold]]))

; START:my-reduce
(defn my-reduce
  ([f coll] (coll-reduce coll f))
  ([f init coll] (coll-reduce coll f init)))
; END:my-reduce

; START:my-fold
(defn my-fold
  ([reducef coll]
    (my-fold reducef reducef coll))
  ([combinef reducef coll]
    (my-fold 512 combinef reducef coll))
  ([n combinef reducef coll]
    (coll-fold coll n combinef reducef)))
; END:my-fold

; START:make-reducer
; START:make-folder
(defn make-reducer [reducible transformf]
  (reify
; END:make-reducer
; START_HIGHLIGHT
    CollFold
    (coll-fold [_ n combinef reducef]
      (coll-fold reducible n combinef (transformf reducef)))
; END_HIGHLIGHT

; START:make-reducer
    CollReduce
    (coll-reduce [_ f1]
      (coll-reduce reducible (transformf f1) (f1)))
    (coll-reduce [_ f1 init]
      (coll-reduce reducible (transformf f1) init))))
; END:make-folder
; END:make-reducer

; START:my-map
(defn my-map [mapf reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (reducef acc (mapf v))))))
; END:my-map

; START:my-filter
(defn my-filter [filterf reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (if (filterf v)
          (reducef acc v)
          acc)))))
; END:my-filter

; START:my-flatten
(defn my-flatten [reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (if (sequential? v)
          (coll-reduce (my-flatten v) reducef acc)
          (reducef acc v))))))
; END:my-flatten
