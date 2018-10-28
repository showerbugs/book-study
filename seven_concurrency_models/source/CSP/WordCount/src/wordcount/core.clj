(ns wordcount.core
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [clojure.java.io :as io]
            [wordcount.feed :refer [new-links]]
            [wordcount.http :refer [http-get]]
            [wordcount.words :refer [get-words]]))

; START:getcounts
(defn get-counts [urls]
  (let [counts (chan)]
    (go (while true
          (let [url (<! urls)]
            (when-let [response (<! (http-get url))]
              (let [c (count (get-words (:body response)))]
                (>! counts [url c]))))))
    counts))
; END:getcounts

; START:main
(defn -main [feeds-file]
  (with-open [rdr (io/reader feeds-file)] ; <label id="code.withopen"/>
    (let [feed-urls (line-seq rdr) ; <label id="code.lineseq"/>
          article-urls (doall (map new-links feed-urls)) ; <label id="code.articleurls"/>
          article-counts (doall (map get-counts article-urls)) ; <label id="code.articlecounts"/>
          counts (async/merge article-counts)] ; <label id="code.merge"/>
      (while true ; <label id="code.outputresults"/>
        (println (<!! counts))))))
; END:main
