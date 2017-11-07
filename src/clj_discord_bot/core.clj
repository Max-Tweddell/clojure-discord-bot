(ns clj-discord-bot.core
  (:gen-class)
  (:require [clj-discord-bot.discord :as discord]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clj-discord-bot.db :refer [db]]
            ;;   [clj-discord-bot.scraper :as scraper]
            [clj-discord-bot.db.messages :as messages]
            [clojure.string :as str])
  (:import [org.postgresql.util.PGobject]))

(defonce token (.trim (slurp "token.txt")))

(defn d100 [type data]
  (discord/answer-command data "!d100" (str "Here you are a random number between 1 and 100: " (inc (rand-int 100)))))

(defn d20 [type data]
  (discord/answer-command data "!d20" (str "here u are a randem numbur bigger than 1 (one)  but littler than tweny sex: " (inc (rand-int 27)))))

(defn command-test [type data]
  (let [command (get data "content")]
    (discord/answer-command data "!blop" (str "blop " (str command)))))

(defn lmgtfy [data command]
  (let [channel_id (get data "channel_id") message (get data "id")]
    (discord/answer-command data "lmgtfy" (str "http://lmgtfy.com/?q=darude+sandstorm" (str/replace command " " "+")))))

(defn get-random-message [type data]
  (let [commmand (get data "content") message (first (messages/random-message db))]
    (do
      (println message)
      (discord/answer-command data "random" (str (:username message) ": " (:content message))))))

(defn void [type data]
  (let [server (get data "channel_id")]
    (if (= server "324776471883415552")
      (discord/delete-message data))))

(defn get-random-number [type data]
  (let [command (get data "content")]
    (discord/answer-command data "getRandomNumber()" (str "Here you are, a random number : " 4))))

(defn rot13 [in]
  (let [A (set "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
        Am (->> (cycle A) (drop 26) (take 52) (zipmap A))]
    (str/join (map (fn* [p1__56363#] (Am p1__56363# p1__56363#)) in))))

(defn encrypt [type data]
  (let [command (get data "content") args (str/join " " (rest (str/split (get data "content") #" ")))]
    (discord/answer-command data "encrypt" (rot13 args))))

(defn decrypt [type data]
  (let [command (get data "content") args (str/join " " (rest (str/split (get data "content") #" ")))]
    (try (discord/answer-command data "decrypt" (str args "->" (rot13 args))) (catch Exception e (println "args")))))

(defn log-event [type data]
  (do
    (println "\nReceived: " type " -> " data)
    (messages/insert-message db {:message (json/generate-string data)})))

(defn repler [type data]
  (let [command (get data "content")
        args (str/join " " (rest (str/split (get data "content") #" ")))
        eval-result (eval (read-string args))]

    (try
      (do
        (discord/answer-command data "eval" (str eval-result))
        (println eval-result)
        (println args))
      (catch Exception e (println e)))))

(defn -main [& args]
  (discord/connect {:token token
                    :functions {"MESSAGE_UPDATE" [d20 d100 command-test void log-event]
                                "MESSAGE_CREATE" [repler d100 command-test void get-random-number log-event encrypt decrypt get-random-message]}}))

                                        ;(discord/disconnect)
