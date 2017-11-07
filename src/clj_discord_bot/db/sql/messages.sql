-- src/clojure-discord-bot/db/sql/messages.sql


-- :name insert-message :!
-- :doc Insert a single character
insert into messages (info)
values (:message)
--WITHOUT FUNCTION AS IMPLICIT

-- :name random-message :? :raw
-- :doc get a random message
select
info ->> 'content' as content,
info -> 'author' ->> 'username' as username
from
messages
where
info ->> 'channel_id' <> '324776471883415552'
order by
random()
limit 1
