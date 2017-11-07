(ns clj-discord-bot.scraper
  (:require [clj-discord-bot.discord :as discord]
            [cheshire.core :as json]
            [clj-discord-bot.db :refer [db]]
            [clj-discord-bot.db.scraperdb :as scraper]))

(def current-message (atom 0))

(defn get-100-messages [type data]
  (let [messages (discord/get-previous-messages (:channel_id data) (:message_id data))]
    (do
    ;;  (scraper/insert-message db {:message (json/generate-string data)})
      (println (str "scraper" type data))
      (swap! current-message inc))))
(defn start []
  (get-100-messages))
