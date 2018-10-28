(ns wiki.contributors
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

(defn start-elem? [event name]
  (and (= :start-element (:type event)) (= name (:name event))))

(defn end-elem? [event name]
  (and (= :end-element (:type event)) (= name (:name event))))

(defn complete? [contribution]
  (and (contains? contribution :username)
       (contains? contribution :contributor-id)))

(defn get-contributions 
  ([in]
    (get-contributions [:top {}] (xml/source-seq in)))
  ([[state contribution] events]
    (lazy-seq
      (let [event (first events)]
        (cond
          (and (= state :top) (start-elem? event :revision))
            (get-contributions [:got-revision {}] (rest events))

          (and (= state :got-revision) (start-elem? event :id))
            (get-contributions [:got-revision
                                (assoc contribution :id (:str (second events)))]
                               (nthrest events 2))

          (and (= state :got-revision) (start-elem? event :timestamp))
            (get-contributions [:got-revision
                                (assoc contribution :timestamp (:str (second events)))]
                               (nthrest events 2))
          
          (and (= state :got-revision) (start-elem? event :contributor))
            (get-contributions [:got-contributor contribution] (rest events))
            
          (and (= state :got-contributor) (start-elem? event :id))
            (get-contributions [:got-contributor 
                                (assoc contribution :contributor-id (:str (second events)))]
                               (nthrest events 2))
            
          (and (= state :got-contributor) (start-elem? event :username))
            (get-contributions [:got-contributor
                                (assoc contribution :username (:str (second events)))]
                               (nthrest events 2))
            
          (and (= state :got-contributor) (end-elem? event :contributor))
            (if (complete? contribution)
              (cons contribution (get-contributions [:got-revision (dissoc contribution :contributor-id :username)]
                                                    (rest events)))
              (get-contributions [:got-revision (dissoc contribution :contributor-id :username)]
                                 (rest events)))
            
          (and (= state :got-revision) (end-elem? event :revision))
            (get-contributions [:top {}] (rest events))
            
          (nil? event) '()
            
          :else (get-contributions [state contribution] (rest events)))))))

