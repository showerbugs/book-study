(ns wordcount.pages
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

(defn- parent->children [parent tag]
  (filter #(= tag (:tag %)) (:content parent)))

(defn- parent->child [parent tag]
  (first (parent->children parent tag)))

(defn- content [tag]
  (first (:content tag)))

(defn- text [page]
  (let [revision (parent->child page :revision)
        text (parent->child revision :text)]
    (content text)))

(defn get-pages [filename]
  (let [in (io/reader filename)
        page-tags (parent->children (xml/parse in) :page)]
    (map text page-tags)))