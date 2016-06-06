(ns twitter-bots.queries.query-defs
  (:require [hugsql.core :as hugsql]
            [environ.core :refer [env]]))

(def db (env :database-url))

(hugsql/def-db-fns "twitter_bots/tables/steve_castle/status_ids.sql")
(hugsql/def-db-fns "twitter_bots/queries/steve_castle/status_ids.sql")
