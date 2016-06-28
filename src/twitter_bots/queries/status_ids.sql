-- :name get-last-responded-to-status-id
-- :command :query
-- :result :one
-- :doc Selects the id of the last responded to status for the given user
SELECT status_id
FROM status_responded_to
WHERE username = :username;

-- :name update-last-responded-to-status-id!
-- :command :insert
-- :result :raw
-- :doc Inserts or updates the id of the last status that has been responded to for the given user
INSERT INTO status_responded_to(username, status_id)
VALUES(:username, :status_id)
ON CONFLICT(username)
DO UPDATE SET status_id=:status_id;
