(defproject csp-wordcount "1.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [rome/rome "1.0"]
                 [http-kit "2.1.16"]]
  :main wordcount.core
  :jvm-opts ^:replace ["-Xms4G" "-Xmx4G" "-XX:NewRatio=8" "-server"])
