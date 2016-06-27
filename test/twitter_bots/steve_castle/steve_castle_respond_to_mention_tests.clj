(ns twitter-bots.steve-castle.steve-castle-respond-to-mention-tests
  (:require [clojure.test :refer :all]
            [twitter-bots.route-functions.steve-castle.respond-to-mention :as unit-test]))

(deftest unit-test-respond-to-mention

  (testing "test generic-response returns expected output"
    (let [valid-options ["@Jarrod We can dance! Dun dun dun dun dun dunun dun dun..."
                         "Awesome @Jarrod. Awesome to the max."
                         "I'll handle this, @Jarrod. You get back to the farm, shift some paradigms, revolutionize outside the box."
                         "@Jarrod I'm proud to be the shepherd of this herd of sharks."
                         "@Jarrod I have no idea what you're talking about, but I love your enthusiasm!"
                         "OK @Jarrod, we've got the hot tub hot, the wine coolers cool. It's Hammer Time!"]]
      (dotimes [n 100]
        (let [response (unit-test/generic-response "Jarrod")]
          (is (= true (some #(= response %) valid-options)))))))

  (testing "test custom-response returns expected output"
    (let [valid-options ["@Jarrod Don't you worry about passing tests Let me worry about passing tests."
                         "@Jarrod passing tests? passing tests?! You're not looking at the big picture!"]]
      (dotimes [n 50]
        (let [response (unit-test/custom-response "Jarrod" "I'm worried about passing tests")]
          (is (= true (some #(= response %) valid-options))))))))
