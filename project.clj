(defproject clj-discord-bot "0.1.0-SNAPSHOT"
  :dependencies [[clj-discord "0.1.1-SNAPSHOT"]
                 [cheshire "5.7.1"]
                 [org.clojure/clojure "1.8.0"]
                 [clj-http "3.6.1"]
                 [org.clojure/java.jdbc "0.7.0"]
                 [com.layerware/hugsql "0.4.7"]
                 [org.postgresql/postgresql "9.4.1212"]
                 [environ "1.1.0"]]
  :main clj-discord-bot.core
  :plugins [[jonase/eastwood "0.2.4"][lein-kibit "0.1.5"][venantius/ultra "0.5.1"]]
  :profiles {:uberjar {:aot [clj-discord-bot.core]}})
