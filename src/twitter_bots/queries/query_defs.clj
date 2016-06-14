(ns twitter-bots.queries.query-defs
  (:require [hugsql.core :as hugsql]
            [environ.core :refer [env]]))

(def db (env :database-url))

(hugsql/def-db-fns "twitter_bots/tables/status_ids.sql")
(hugsql/def-db-fns "twitter_bots/queries/status_ids.sql")
