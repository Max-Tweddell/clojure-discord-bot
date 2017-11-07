(ns clj-discord-bot.db
  (:require [clojure.java.jdbc :as j]
            [hugsql.core :as hugsql]))

(let [db-host "172.93.48.105"
      db-port 5432
      db-name "discord"]

  (def db {:classname "org.postgresql.Driver" ; must be in classpath
           :subprotocol "postgresql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "peon"
           :stringtype "unspecified"
           :password "potato"}))
