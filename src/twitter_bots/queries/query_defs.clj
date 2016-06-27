(ns twitter-bots.queries.query-defs
  (:require [hugsql.core :as hugsql]
            [environ.core :refer [env]]))

(def db (env :database-url))

(hugsql/def-db-fns "twitter_bots/tables/status_ids.sql")
(hugsql/def-db-fns "twitter_bots/tables/paper_rock_stats.sql")
(hugsql/def-db-fns "twitter_bots/queries/status_ids.sql")
(hugsql/def-db-fns "twitter_bots/queries/truncate_all.sql")
(hugsql/def-db-fns "twitter_bots/queries/paper_rock_stats.sql")
