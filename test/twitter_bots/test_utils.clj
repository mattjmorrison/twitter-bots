(ns twitter-bots.test-utils
  (:require [twitter-bots.queries.query-defs :as query]))


(defn create-tables [f]
  (query/create-paper-rock-stats-table-if-not-exists! query/db)
  (query/create-status-responded-to-table-if-not-exists! query/db)
  (f))
