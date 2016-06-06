(defproject twitter-bots "0.1.0-SNAPSHOT"
  :description "A collection of amusing twitter bots"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure        "1.8.0"]
                 [metosin/compojure-api      "1.1.2"]
                 [metosin/ring-http-response "0.6.5"]
                 [metosin/ring-swagger-ui    "2.1.4-0"]
                 [http-kit                   "2.1.19"]
                 [twitter-api                "0.7.8"]
                 [org.clojure/java.jdbc      "0.6.1"]
                 [postgresql/postgresql      "9.3-1102.jdbc41"]
                 [com.layerware/hugsql       "0.4.7"]
                 [environ                    "1.0.3"]]

  :plugins      [[lein-environ "1.0.2"]
                 [funcool/codeina "0.3.0" :exclusions [org.clojure/clojure]]]

  :min-lein-version  "2.5.0"

  :uberjar-name "server.jar"

  :codeina {:sources ["src"]
            :reader :clojure}

  :profiles {:uberjar {:resource-paths ["swagger-ui"]
                       :aot :all}

             :test-local   {:dependencies [[javax.servlet/servlet-api "2.5"]
                                           [ring-mock                 "0.1.5"]]}

             ;; Set these in ./profiles.clj
             :test-env-vars {}
             :dev-env-vars  {}

             :test       [:test-local :test-env-vars]
             :dev        [:dev-env-vars :test-local]
             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}}}

  :test-selectors {:default (constantly true)
                   :wip     :wip})
