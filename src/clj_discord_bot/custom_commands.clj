(ns clj-discord-bot.custom-commands
  (:require [clojure.java.io :as io]
            [clj-discord-bot.discord :as discord]
            [cheshire.core :as json]))

(println "hello")

(def command-file (slurp "data.json"))

(defn read-data []
  (->> (json/parse-string command-file)
       (seq)
       (map (fn [data] (create-command (symbol (first data)) (last data))))
       (map var-get)
       (vec)))

(defmacro create-command [name result]
  (let [name-sym (gensym "name")
        type-sym (gensym "type")
        data-sym (gensym "data")]
    `(defn ~name-sym [~type-sym ~data-sym] (discord/answer-command ~data-sym ~name (str ~result)))))
