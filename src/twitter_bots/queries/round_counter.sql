-- :name increment-round-number!
-- :command :insert
-- :result :raw
-- :doc inserts or updates the current round number
INSERT INTO round_counter(id, current_count)
VALUES(1, 1)
ON CONFLICT(id)
DO UPDATE SET current_count = round_counter.current_count + 1;
