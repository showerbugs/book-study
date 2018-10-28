(ns wiki.core
  (:require [clojure.edn :as edn]
            [wiki.pages :refer :all]))

(defn -main [& args]
  (try
    (let [[infile outfile page-count] args
          pages (get-pages infile)
          trimmed-pages (if page-count (take (edn/read-string page-count) pages) pages)]
      (doseq [page trimmed-pages]
        (spit outfile page :append true)))
    (catch Exception _ (println "Usage: <infile> <outfile> <page-count?>"))))
