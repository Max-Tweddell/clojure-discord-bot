(ns clj-discord-bot.discord
  (:gen-class)
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [gniazdo.core :as ws]
            [taoensso.carmine :as car :refer (wcar)]
            [clojure.string :as str]))

(defonce the-token (atom nil))
(defonce the-gateway (atom nil))
(defonce the-socket (atom nil))
(defonce the-heartbeat-interval (atom nil))
(defonce the-keepalive (atom false))
(defonce the-seq (atom nil))
(defonce reconnect-needed (atom false))

(defn write-file [content]
  (with-open [w (clojure.java.io/writer  "errors.txt" :append true)]
    (.write w (str content))))
(defn disconnect []
  (reset! reconnect-needed false)
  (reset! the-keepalive false)
  (if (not (nil? @the-socket)) (ws/close @the-socket))
  (reset! the-token nil)
  (reset! the-gateway nil)
  (reset! the-socket nil)
  (reset! the-seq nil)
  (reset! the-heartbeat-interval nil))

(defn connect [token functions log-events]
  (disconnect)
  (reset! the-keepalive true)
  (reset! the-token (str "Bot " token))
  (reset! the-gateway (str
                       (get
                        (json/decode
                         (:body (http/get "https://discordapp.com/api/gateway"
                                          {:headers {:authorization @the-token}})))
                        "url")
                       "?v=6&encoding=json"))
  (reset! the-socket
          (ws/connect
           @the-gateway
           :on-receive #(let [received (json/decode %)
                              logevent (if log-events (println "\n" %))
                              op       (get received "op")
                              type     (get received "t")
                              data     (get received "d")
                              seq      (get received "s")]
                          (if (= 10 op) (reset! the-heartbeat-interval (get data "heartbeat_interval")))
                          (if (not (nil? seq)) (reset! the-seq seq))
                          (if (not (nil? type)) (future (doseq [afunction (get functions type (get functions "ALL_OTHER" []))]  (afunction type data) ()))))))
  (.start (Thread. (fn []
                     (try
                       (while @the-keepalive
                         (if (nil? @the-heartbeat-interval)
                           (Thread/sleep 100)
                           (do
                             (if log-events (println "\nSending heartbeat " @the-seq))
                             (ws/send-msg @the-socket (json/encode {:op 1, :d @the-seq}))
                             (Thread/sleep @the-heartbeat-interval))))
                       (catch Exception e (do
                                            (println "\nCaught exception: " (.getMessage e) " -> " (str e))
                                            (write-file (.getMessage e))
                                            (reset! reconnect-needed true)))))))
  (Thread/sleep 10)
  (ws/send-msg @the-socket (json/encode {:op 2, :d {"token"      @the-token

                                                    "properties" {"$os"               "linux"
                                                                  "$browser"          "clj-discord"
                                                                  "$device"           "clj-discord"
                                                                  "$referrer"         ""
                                                                  "$referring_domain" ""}
                                                    "compress"   false}}))
  (while (not @reconnect-needed) (Thread/sleep 10))
  (connect token functions log-events))

(defn get-previous-messages [channel_id message]
  "gets channel messages before selected message"
  (http/get (str "https://discordapp.com/api/channels" channel_id "/messages")
            {:body (json/encode {:before (:id message)})
             :headers {:authorization @the-token}
             :content-type :json
             :accept :json})

  )
(defn post-message [channel_id message]
  (http/post (str "https://discordapp.com/api/channels/" channel_id "/messages")
             {:body (json/encode {:content message
                                     :nonce (str (System/currentTimeMillis))
                                     :tts false})
              :headers {:authorization @the-token}
              :content-type :json
              :accept :json}))
(defn post-message-with-mention [channel_id message user_id]
  (post-message channel_id (str  user_id "-> " message)))

(defn post-message-without-mention [channel_id message user_id]
  (post-message channel_id (str message)))
(defn answer-command [data command answer]
  (if (re-find (re-pattern (str "^" command)) (get data "content"))
    (post-message-with-mention
     (get data "channel_id")
     (str " " answer)
     (get (get data "author") "username"))))

(defn get-arguments [data command answer]
  (first (rest (str/split (get data "content") #" "))))

(defn delete-message [data]
  (let [channel_id (get data "channel_id") message (get data "id")]
    (do
      (Thread/sleep 17900)
      (http/delete (str "https://discordapp.com/api/channels/" channel_id "/messages/" message "?token=" @the-token) {:throw-exceptions false}))))

(defn answer-message "for filtering, searchs the whole message not just the start" [data command answer]
  (if (re-find (re-pattern (str "(?i)"  command)) (get data "content"))
    (post-message-without-mention
     (get data "channel_id")
     (str " " answer)
     (get (get data "author") "id"))))
(defn answer [data answer]
  (post-message-with-mention
   (get data "channel_id")
   (str "=> " answer)
   (get (get data "author") "username")))
