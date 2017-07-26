(defproject clj-discord-example "0.1.0-SNAPSHOT"
  :dependencies [[clj-discord "0.1.1-SNAPSHOT"]
                 [cheshire "5.7.1"]
                 [clj-http "3.6.1"]
                 [com.layerware/hugsql "0.4.7"]
                 [environ "1.1.0"]]
  :main clj-discord-bot.core
  :profiles {:uberjar {:aot [clj-discord-bot.core]}}
  :user {:env {:forecast-key "e0971c18e4fe3fa0bf9cbb286be291b6"}})
