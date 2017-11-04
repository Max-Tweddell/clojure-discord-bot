(ns clj-discord-bot.db.scraperdb
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "clj_discord_bot/db/sql/scraper.sql")
