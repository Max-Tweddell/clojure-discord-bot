-- src/clojure-discord-bot/db/sql/message.sql

-- :name insert-message :!
-- :doc Insert a single message
insert into history (info)
values (:message)
