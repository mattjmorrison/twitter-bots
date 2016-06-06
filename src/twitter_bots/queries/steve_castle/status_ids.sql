-- :name get-last-responded-to-status-id
-- :command :query
-- :result :one
-- :doc Selects the idea of the last responded to status
SELECT status_id
FROM status_responded_to_by_steve_castle
WHERE id = 1;

-- :name update-last-responded-to-status-id!
-- :command :insert
-- :result :raw
-- :doc Inserts or updates the id of the last status that has been responded to
INSERT INTO status_responded_to_by_steve_castle(id, status_id)
VALUES(1, :status_id)
ON CONFLICT(id)
DO UPDATE SET status_id=:status_id;
