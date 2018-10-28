(ns hello-clojurescript.core
  (:require-macros [cljs.core.async.macros :refer [go]]) ; <label id="code.requiremacros"/>
  (:require [goog.dom :as dom]
            [cljs.core.async :refer [<! timeout]])) ; <label id="code.require"/>
			
(defn output [elem message] ; <label id="code.append"/>
  (dom/append elem message (dom/createDom "br")))
(defn start []
  (let [content (dom/getElement "content")]
    (go 
      (while true 
        (<! (timeout 1000)) 
        (output content "Hello from task 1"))) ; <label id="code.go1"/>
    (go 
      (while true 
        (<! (timeout 1500)) 
        (output content "Hello from task 2"))))) ; <label id="code.go2"/>

(set! (.-onload js/window) start) ; <label id="code.onload"/>
