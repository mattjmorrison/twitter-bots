(ns twitter-bots.routes.paper-rock-bot
  (:require [twitter-bots.middleware.cors :refer [cors-mw]]
            [twitter-bots.route-functions.paper-rock-bot.respond-to-mention :refer [respond-to-mention-response]]
            [schema.core :as s]
            [compojure.api.sweet :refer :all]))

(def paper-rock-bot-routes
  "Routes that deal with the PaperRockBot twitter user"
  (context "/api/paper-rock-bot" []
           :tags ["PaperRockBot"]

    (POST "/"           {:as request}
           :return      {:message String}
           :middleware  [cors-mw]
           :body-params [poke :- String]
           :summary     "Response to the user that mentioned @PaperRockBot"
           (respond-to-mention-response))))
