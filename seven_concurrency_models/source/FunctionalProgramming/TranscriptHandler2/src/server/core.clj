(ns server.core
  (:require [clojure.edn        :as    edn]
            [server.sentences   :refer [strings->sentences]]
            [server.charset     :refer [wrap-charset]]
            [compojure.core     :refer :all]
            [compojure.handler  :refer [api]]
            [ring.util.response :refer [charset response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client    :as client]))

; START:core
(def snippets (repeatedly promise))

; START:translate
(def translator "http://localhost:3001/translate")

(defn translate [text]
  (future
    (:body (client/post translator {:body text}))))
; END:translate

; START:translations
(def translations
  (delay
    (map translate (strings->sentences (map deref snippets)))))
; END:translations

(defn accept-snippet [n text]
  (deliver (nth snippets n) text))

(defn get-translation [n] ; <label id="code.get-translation"/>
  @(nth @translations n))

(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
    (accept-snippet (edn/read-string n) (slurp body))
    (response "OK"))
  (GET "/translation/:n" [n] ; <label id="code.translation"/>
    (response (get-translation (edn/read-string n)))))

(defn -main [& args]
  (run-jetty (wrap-charset (api app-routes)) {:port 3000}))
; END:core