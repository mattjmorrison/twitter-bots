-- :name create-status-responded-to-table-if-not-exists!
-- :command :execute
-- :result :affected
-- :doc Create the status_responded_to table if it does not exist
CREATE TABLE IF NOT EXISTS status_responded_to (
    id           SERIAL  PRIMARY KEY
    , username   TEXT UNIQUE NOT NULL
    , status_id  BIGINT);
