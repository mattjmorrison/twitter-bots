(ns twitter-bots.route-functions.steve-castle.respond-to-mention
  (:require [twitter-bots.queries.query-defs :as query]
            [clojure.string :as str]
            [twitter-bots.route-functions.general-functions :as general]
            [twitter.oauth :as twitter-oauth]
            [environ.core :refer [env]]
            [ring.util.http-response :as respond]))

(def bot-creds (twitter-oauth/make-oauth-creds (env :steve-castle-app-consumer-key)
                                               (env :steve-castle-app-consumer-secret)
                                               (env :steve-castle-user-access-token)
                                               (env :steve-castle-user-access-secret)))

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

(defn respond-to-mention [mention]
  (if (str/includes? (:text mention) "worried about")
    (general/post-status (custom-response (:screen-name mention) (:text mention)) bot-creds)
    (general/post-status (generic-response (:screen-name mention)) bot-creds)))

(defn respond-to-mention-response []
  (let [last-id-responded-to  (query/get-last-responded-to-status-id query/db {:username "SteveCastleCEO"})
        mentions              (general/get-mentions (:status_id last-id-responded-to) bot-creds)
        last-id-from-mentions (apply max (map :id mentions))]
    (query/update-last-responded-to-status-id! query/db {:status_id last-id-from-mentions :username "SteveCastleCEO"})
    (doseq [mention mentions]
      (respond-to-mention mention))
    (respond/ok {:message "The response has been sent"})))
