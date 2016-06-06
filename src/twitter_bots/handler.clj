(ns twitter-bots.handler
  (:require [compojure.api.sweet :refer :all]
            [twitter-bots.routes.steve-castle :refer :all]
            [twitter-bots.routes.preflight :refer :all]
            [twitter-bots.middleware.cors :refer [cors-mw]]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(defapi app
  {:swagger
   {:ui   "/api-docs"
    :spec "/swagger.json"
    :data {:info {:title "twitter-bots"
                  :version "0.0.1"}
           :tags [{:name "Preflight"     :description "Return successful response for all preflight requests"}
                  {:name "Steve Castle"  :description "Steve Castle twitter bot"}]}}}
  preflight-route
  steve-castle-routes)
