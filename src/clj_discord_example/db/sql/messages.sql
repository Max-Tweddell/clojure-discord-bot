-- src/clojure-discord-example/db/sql/messages.sql


-- :name insert-message :! :n
-- :doc Insert a single character
insert into messages (info)
values (:message)
--WITHOUT FUNCTION AS IMPLICIT

-- :name random-message :! :n
-- :doc get a random message
select
info -> 'content' as content
from
messages
where
info -> 'author' ->> 'username' = 'vestigneo'
order by
random()
limit 1
