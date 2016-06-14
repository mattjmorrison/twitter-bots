-- :name create-status-responded-to-table-if-not-exists!
-- :command :execute
-- :result :affected
-- :doc Create the status_responded_to table if it does not exist
CREATE TABLE IF NOT EXISTS status_responded_to (
    id           SERIAL  PRIMARY KEY
    , username   TEXT UNIQUE NOT NULL
    , status_id  BIGINT);

-- :name drop-status-responded-to-table!
-- :command :execute
-- :result :affected
-- :doc Drop the status_responded_to table
DROP TABLE status_responded_to CASCADE;
