-- :name create-round-counter-table-if-not-exists!
-- :command :execute
-- :result :affected
-- :doc Create the round_counter table if it does not exist
CREATE TABLE IF NOT EXISTS round_counter (
    id               SERIAL  PRIMARY KEY
    ,  current_count INTEGER);
