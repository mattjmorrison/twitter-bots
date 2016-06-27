-- :name create-paper-rock-stats-table-if-not-exists!
-- :command :execute
-- :result :affected
-- :doc Create the paper_rock_stats table if it does not exist
CREATE TABLE IF NOT EXISTS paper_rock_stats (
    id               SERIAL  PRIMARY KEY
    , username       TEXT UNIQUE NOT NULL
    , current_streak INTEGER
    , last_updated   TIMESTAMP);
