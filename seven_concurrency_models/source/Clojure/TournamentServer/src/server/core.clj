(ns server.core
  (:require [compojure.core         :refer :all]
            [compojure.handler      :refer [site]]
            [ring.util.response     :refer [response status]]
            [ring.adapter.jetty     :refer [run-jetty]]
            [cheshire.core          :as json]))

; START:core
(def players (atom ())) ; <label id="code.players"/>

(defn list-players []
  (response (json/encode @players))) ; <label id="code.list-players"/>

(defn create-player [player-name]
  (swap! players conj player-name) ; <label id="code.create-player"/>
  (status (response "") 201))

(defroutes app-routes ; <label id="code.app-routes"/>
  (GET "/players" [] (list-players))
  (PUT "/players/:player-name" [player-name] (create-player player-name)))
(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000})) ; <label id="code.run-jetty"/>
; END:core