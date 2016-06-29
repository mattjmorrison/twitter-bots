(ns twitter-bots.server
  (:require [org.httpkit.server :as httpkit]
            [twitter-bots.queries.query-defs :as query]
            [twitter-bots.handler :refer [app]]))

(defn create-tables
  "Create database tables if they don't exist"
  []
  (query/create-round-counter-table-if-not-exists! query/db)
  (query/create-paper-rock-stats-table-if-not-exists! query/db)
  (query/create-status-responded-to-table-if-not-exists! query/db))

(defn -main [port]
  (create-tables)
  (httpkit/run-server app {:port (Integer/parseInt port) :join false})
  (println "server started on port:" port))
