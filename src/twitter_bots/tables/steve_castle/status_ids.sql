-- :name create-status-responded-to-by-steve-castle-table-if-not-exists!
-- :command :execute
-- :result :affected
-- :doc Create the status_responded_to_by_steve_castle table if it does not exist
CREATE TABLE IF NOT EXISTS status_responded_to_by_steve_castle (
    id           SERIAL  PRIMARY KEY
    , status_id  BIGINT);

-- :name drop-status-responded-to-by-steve-castle-table!
-- :command :execute
-- :result :affected
-- :doc Drop the status_responded_to_by_steve_castle table
DROP TABLE status_responded_to_by_steve_castle CASCADE;
