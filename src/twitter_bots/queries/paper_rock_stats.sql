-- :name increment-users-current-streak!
-- :command :insert
-- :result :raw
-- :doc Inserts or updates the current_streak for the specified user
INSERT INTO paper_rock_stats(username, current_streak, last_updated)
VALUES(:username, 1, NOW())
ON CONFLICT(username)
DO UPDATE SET
    current_streak = paper_rock_stats.current_streak + 1
    , last_updated = NOW();

-- :name reset-users-current-streak!
-- :command :insert
-- :result :raw
-- :doc Inserts or updates the current_streak for the specified user
INSERT INTO paper_rock_stats(username, current_streak, last_updated)
VALUES(:username, 0, NOW())
ON CONFLICT(username)
DO UPDATE SET
    current_streak = 0
    , last_updated = NOW();

-- :name get-recordholder-stats
-- :command :query
-- :result :one
-- :doc selects the row for the current record holder
SELECT * FROM paper_rock_stats
ORDER BY
  current_streak DESC
  , last_updated ASC
LIMIT 1;

-- :name get-users-current-streak
-- :command :query
-- :result :one
-- :doc selects the row for the specified username
SELECT * FROM paper_rock_stats
WHERE username = :username;
