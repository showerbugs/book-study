(ns wordcount.feed
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [clojure.java.io :as io]
            [wordcount.polling :refer [poll]]
            [wordcount.http :refer [http-get]])
  (:import [com.sun.syndication.io XmlReader SyndFeedInput]))

; START:pollfeed
(def poll-interval 60)

; END:pollfeed
(defn get-links [feed]
  (map #(.getLink %) (.getEntries feed)))

(defn parse-feed [body]
  (let [reader (XmlReader. (io/input-stream (.getBytes body)))]
    (.build (SyndFeedInput.) reader)))

; START:pollfeed
; Simple-minded feed-polling function
; WARNING: Don't use in production (use conditional get instead)

(defn poll-feed [url]
  (let [ch (chan)]
    (poll poll-interval
      (when-let [response (<! (http-get url))]
        (let [feed (parse-feed (:body response))]
          (onto-chan ch (get-links feed) false))))
    ch))
; END:pollfeed

; START:newlinks
(defn new-links [url]
  (let [in (poll-feed url)
        out (chan)]
    (go-loop [links #{}]
      (let [link (<! in)]
        (if (contains? links link)
          (recur links)
          (do
            (>! out link)
            (recur (conj links link))))))
    out))
; END:newlinks
