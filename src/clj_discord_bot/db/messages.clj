(ns clj-discord-bot.db.messages
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "clj_discord_bot/db/sql/messages.sql")
