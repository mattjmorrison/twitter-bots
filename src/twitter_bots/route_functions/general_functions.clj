(ns twitter-bots.route-functions.general-functions
  (:require [twitter.callbacks.handlers :as twitter-handler]
            [twitter.api.restful :as twitter])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(defn extract-mentioned-values-from-status [status]
  (let [screen-name (get-in status [:user :screen_name])
        text        (:text status)
        id          (:id status)]
    {:id id :screen-name screen-name :text text}))

(defn get-mentions [last-id-responded-to bot-creds]
  (let [statuses (twitter/statuses-mentions-timeline :oauth-creds bot-creds
                                                     :params      {:since_id last-id-responded-to}
                                                     :callbacks   (SyncSingleCallback. twitter-handler/response-return-body
                                                                                       twitter-handler/response-throw-error
                                                                                       twitter-handler/exception-rethrow))]
    (map extract-mentioned-values-from-status statuses)))

(defn post-status [content bot-creds]
  (twitter/statuses-update :oauth-creds bot-creds :params {:status content}))
