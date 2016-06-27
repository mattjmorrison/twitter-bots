(ns twitter-bots.route-functions.paper-rock-bot.respond-to-mention
  (:require [twitter-bots.queries.query-defs :as query]
            [twitter-bots.route-functions.general-functions :as general]
            [clojure.string :as str]
            [twitter.oauth :as twitter-oauth]
            [environ.core :refer [env]]
            [ring.util.http-response :as respond]))

(def bot-creds (twitter-oauth/make-oauth-creds (env :paper-rock-bot-app-consumer-key)
                                               (env :paper-rock-bot-app-consumer-secret)
                                               (env :paper-rock-bot-user-access-token)
                                               (env :paper-rock-bot-user-access-secret)))

(defn user-is-winner? [user-play bot-play]
  (let [winners [["paper" "rock"] ["scissors" "paper"] ["rock" "scissors"]]]
    (cond
      (= user-play bot-play)                     nil
      (some #(= [user-play bot-play] %) winners) true
      :else                                      false)))

(defn update-current-streak [winner? username]
  (cond
    (= winner? nil) (query/get-users-current-streak query/db {:username username})
    winner?         (query/increment-users-current-streak! query/db {:username username})
    (not winner?)   (query/reset-users-current-streak! query/db {:username username})))

(defn response-to-mention [mention]
  (let [record          (query/get-recordholder-stats query/db)
        current-champ?  (= (:username record) (:screen-name mention))
        user-play       (first (re-find #"(?i)(\bpaper\b|\brock\b|\bscissors\b)" (:text mention)))
        bot-play        (rand-nth ["paper" "rock" "scissors"])
        winner?         (user-is-winner? user-play bot-play)
        current-streak  (update-current-streak winner? (:screen-name mention))
        new-champ-stats (query/get-recordholder-stats query/db)
        new-champ?      (= (:username new-champ-stats) (:screen-name mention))]
    (cond
      (= user-play nil)                      (str "@" (:screen-name mention) " Invalid Play: Try again and include the word 'paper', 'rock' or 'scissors' in your tweet.")
      (and (= winner? true) current-champ?)  (str "@" (:screen-name mention) " " user-play " beats "        bot-play " as the reigning champ that bumps your current streak to: " (:current_streak current-streak) ".")
      (and (= winner? true) new-champ?)      (str "@" (:screen-name mention) " " user-play " beats "        bot-play " raising your current streak to: " (:current_streak current-streak) ". Beating the previous champ @" (:username record))
      (= winner? true)                       (str "@" (:screen-name mention) " " user-play " beats "        bot-play " you win. Current streak: " (:current_streak current-streak) ". The current champ has a streak of: " (:current_streak record))
      (and (= winner? false) current-champ?) (str "@" (:screen-name mention) " " user-play " is beaten by " bot-play " you lose. Which makes @" (:username new-champ-stats) " the new champion with a streak of: " (:current_streak new-champ-stats))
      (= winner? false)                      (str "@" (:screen-name mention) " " user-play " is beaten by " bot-play " you lose. Current streak: " (:current_streak current-streak) ". The current champ has a streak of: " (:current_streak record))
      (= winner? nil)                        (str "@" (:screen-name mention) " " user-play " ties with "    bot-play " no winner. Current streak: " (:current_streak current-streak) ". The current champ has a streak of: " (:current_streak record)))))

(defn respond-to-mention-response []
  (let [last-id-responded-to  (query/get-last-responded-to-status-id query/db {:username "PaperRockBot"})
        mentions              (general/get-mentions (or (:status_id last-id-responded-to) 1) bot-creds)
        last-id-from-mentions (apply max (map :id mentions))]
    (query/update-last-responded-to-status-id! query/db {:status_id last-id-from-mentions :username "PaperRockBot"})
    (doseq [mention mentions]
      (general/post-status (response-to-mention mention) bot-creds))
    (respond/ok {:message "The response has been sent"})))
