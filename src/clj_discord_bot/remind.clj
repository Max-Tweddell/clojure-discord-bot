(ns clj-discord-bot.remind
  (:require [clj-discord.core :as discord]
            [clojure.string :as string]))

(def time (agent []))

(defn thing [time]
  (let [answer-time (promise)]
    (dosync (Thread/sleep time) (deliver answer-time true))))
