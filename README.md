# twitter-bots

## Steve Castle A.K.A. "That Guy"

<img src="https://cloud.githubusercontent.com/assets/4416952/15964906/75784396-2ee0-11e6-92b5-e6328d67bcec.png" alt="SteveCastleCEO" title="SteveCastleCEO" align="left" />

Steve Castle is a sleazy wall street figure from the 80s. If you mention him on
twitter `@SteveCastleCEO` he will respond with the hippest 80s executive
non-speak. He is also a life coach and powerful leadership figure. He will
help calm your concerns if you tell him what you are "worried about" for
example `@SteveCastleCEO I am worried about the ethics of this company`

## Running locally

### Add profiles.clj

The project pulls sensitive information from environment variables. For local
development you will need a `profiles.clj` in the root of the project. However,
without the appropriate twitter api keys you will not be able to make the
actual requests successfully. The file would look like:

``` clojure
{:dev-env-vars  {:env {:database-url             "postgres://bot_user:password1@127.0.0.1:5432/twitter_bots?stringtype=unspecified"
                       :app-consumer-key         "twitterApplicationConsumerKey"
                       :app-consumer-secret      "twitterApplicationConsumerSecret"
                       :user-access-token        "twitterUserAccessToken"
                       :user-access-secret       "twitterUserAccessSecretToken"}}
 :test-env-vars {:env {:database-url             "postgres://bot_user:password1@127.0.0.1:5432/twitter_bots_test?stringtype=unspecified"}}}
```
### Running The Server

`lein run -m twitter-bots.server 3000`

Then visit [http://localhost:3000/api-docs/index.html](http://localhost:3000/api-docs/index.html)

### Table migrations / creation

When you start the server any needed tables will be created automatically.

### Running Tests

`lein test`
