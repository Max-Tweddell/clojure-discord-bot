(ns clj-discord-example.db.messages
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "clj_discord_example/db/sql/messages.sql")

