(ns hello-clojurescript.core
  (:require [compojure.core :refer :all]
            [compojure.handler :refer [site]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [redirect]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defroutes app-routes
  (GET "/" [] (redirect "/index.html"))
  (resources "/")
  (not-found "Page not found"))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
