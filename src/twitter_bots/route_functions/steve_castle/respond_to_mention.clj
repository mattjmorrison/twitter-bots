(ns twitter-bots.route-functions.steve-castle.respond-to-mention
  (:require [twitter-bots.queries.query-defs :as query]
            [clojure.string :as str]
            [twitter.callbacks.handlers :as twitter-handler]
            [twitter.api.restful :as twitter]
            [twitter.oauth :as twitter-oauth]
            [environ.core :refer [env]]
            [postal.core :refer [send-message]]
            [ring.util.http-response :as respond])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(def my-creds (twitter-oauth/make-oauth-creds (env :app-consumer-key)
                                              (env :app-consumer-secret)
                                              (env :user-access-token)
                                              (env :user-access-secret)))

(defn generic-response [screen-name]
  (let [responses [(str "@" screen-name " We can dance! Dun dun dun dun dun dunun dun dun...")
                   (str "Awesome @" screen-name ". Awesome to the max.")
                   (str "I'll handle this, @" screen-name ". You get back to the farm, shift some paradigms, revolutionize outside the box.")
                   (str "@" screen-name " I'm proud to be the shepherd of this herd of sharks.")
                   (str "@" screen-name " I have no idea what you're talking about, but I love your enthusiasm!")
                   (str "OK @" screen-name ", we've got the hot tub hot, the wine coolers cool. It's Hammer Time!")]]
    (rand-nth responses)))

(defn custom-response [screen-name text]
  (let [subject   (last (re-find #"worried about (.+)" text))
        responses [(str "@" screen-name " Don't you worry about " subject " Let me worry about " subject ".")
                   (str "@" screen-name " " subject "? " subject "?! You're not looking at the big picture!")]]
  (rand-nth responses)))

(defn extract-mentioned-values-from-status [status]
  (let [screen-name (get-in status [:user :screen_name])
        text        (:text status)
        id          (:id status)]
    {:id id :screen-name screen-name :text text}))

(defn get-mentions [last-id-responded-to]
  (let [statuses (twitter/statuses-mentions-timeline :oauth-creds my-creds
                                                     :params      {:since_id last-id-responded-to}
                                                     :callbacks   (SyncSingleCallback. twitter-handler/response-return-body
                                                                                       twitter-handler/response-throw-error
                                                                                       twitter-handler/exception-rethrow))]
    (map extract-mentioned-values-from-status statuses)))

(defn post-status [content]
  (twitter/statuses-update :oauth-creds my-creds :params {:status content}))

(defn respond-to-mention [mention]
  (if (str/includes? (:text mention) "worried about")
    (post-status (custom-response (:screen-name mention) (:text mention)))
    (post-status (generic-response (:screen-name mention)))))

(defn respond-to-mention-response []
  (let [last-id-responded-to  (query/get-last-responded-to-status-id query/db)
        mentions              (get-mentions (:status_id last-id-responded-to))
        last-id-from-mentions (apply max (map :id mentions))]
    (query/update-last-responded-to-status-id! query/db {:status_id last-id-from-mentions})
    (doseq [mention mentions]
      (respond-to-mention mention))
    (respond/ok {:message "The response has been sent"})))
