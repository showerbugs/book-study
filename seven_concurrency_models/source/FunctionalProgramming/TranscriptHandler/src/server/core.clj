(ns server.core
  (:require [clojure.edn        :as    edn]
            [compojure.core     :refer :all]
            [compojure.handler  :refer [site]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]))

; START:snippets
(def snippets (repeatedly promise))
; END:snippets

; START:print-snippets
(future 
  (doseq [snippet (map deref snippets)]
    (println snippet)))
; END:print-snippets

; START:accept-snippet
(defn accept-snippet [n text]
  (deliver (nth snippets n) text))
; END:accept-snippet

; START:webservice
(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
    (accept-snippet (edn/read-string n) (slurp body))
    (response "OK")))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
; END:webservice