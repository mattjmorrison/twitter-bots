(ns twitter-bots.routes.steve-castle
  (:require [twitter-bots.middleware.cors :refer [cors-mw]]
            [twitter-bots.route-functions.steve-castle.respond-to-mention :refer [respond-to-mention-response]]
            [schema.core :as s]
            [compojure.api.sweet :refer :all]))

(def steve-castle-routes
  "Routes that deal with the Steve Castle twitter user"
  (context "/api/steve-castle" []
           :tags ["SteveCastle"]

    (POST "/"           {:as request}
           :return      {:message String}
           :middleware  [cors-mw]
           :body-params [poke :- String]
           :summary     "Response to the user that mentioned @SteveCastleCEO"
           (respond-to-mention-response))))
