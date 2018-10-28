(ns wiki.core
  (:require [wiki.contributors :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn -main [& args]
  (try
    (let [[infile outfile] args]
      (with-open [in (io/input-stream infile)
                  out (io/writer outfile)]
        (doseq [contribution (get-contributions in)]
          (.write out (string/join " " [(:timestamp contribution) 
                                        (:id contribution) 
                                        (:contributor-id contribution)
                                        (:username contribution)]))
          (.write out "\n"))))
    (catch Exception _ (println "Usage: <infile> <outfile>"))))
