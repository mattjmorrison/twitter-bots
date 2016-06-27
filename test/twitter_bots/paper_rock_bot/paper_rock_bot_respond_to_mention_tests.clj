(ns twitter-bots.paper-rock-bot.paper-rock-bot-respond-to-mention-tests
  (:require [clojure.test :refer :all]
            [twitter-bots.queries.query-defs :as query]
            [twitter-bots.test-utils :as helper]
            [twitter-bots.route-functions.paper-rock-bot.respond-to-mention :as sut]))

(defn setup-teardown [f]
  (try
    (f)
    (finally (query/truncate-all-tables-in-database! query/db))))

(use-fixtures :once helper/create-tables)
(use-fixtures :each setup-teardown)

(deftest unit-test-response-to-mention-when-invalid-mention
  (testing "test response-to-mention when invalid mention"
    (with-redefs [rand-nth        (fn [_] "rock")]
      (let [mention {:screen-name "Jarrod" :text "I chose poorly"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod Invalid Play: Try again and include the word 'paper', 'rock' or 'scissors' in your tweet." result))))))

(deftest unit-test-response-to-mention-when-non-champion-user-is-winner
  (testing "test response-to-mention when non-champion user is winner"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Snake"}))
    (with-redefs [rand-nth        (fn [_] "rock")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper beats rock you win. Current streak: 1. The current champ has a streak of: 5" result))))))

(deftest unit-test-response-to-mention-when-user-is-winner-and-they-are-the-current-champion
  (testing "test response-to-mention when user is winner and they are the current champion"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Jarrod"}))
    (with-redefs [rand-nth        (fn [_] "rock")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper beats rock as the reigning champ that bumps your current streak to: 6." result))))))

(deftest unit-test-response-to-mention-when-non-champion-user-is-winner-and-they-are-the-current-champion
  (testing "test response-to-mention when non-champion user is winner and they are the current champion"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Snake"}))
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Jarrod"}))
    (with-redefs [rand-nth        (fn [_] "rock")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper beats rock raising your current streak to: 6. Beating the previous champ @Snake" result))))))

(deftest unit-test-user-loses-and-was-previous-champion
  (testing "test user loses and was previous champion"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Snake"}))
    (dotimes [_ 6](query/increment-users-current-streak! query/db {:username "Jarrod"}))
    (with-redefs [rand-nth        (fn [_] "scissors")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper is beaten by scissors you lose. Which makes @Snake the new champion with a streak of: 5" result))))))

(deftest unit-test-user-loses-and-was-not-champion
  (testing "test user loses and was not champion"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Snake"}))
    (dotimes [_ 4](query/increment-users-current-streak! query/db {:username "Jarrod"}))
    (with-redefs [rand-nth        (fn [_] "scissors")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper is beaten by scissors you lose. Current streak: 0. The current champ has a streak of: 5" result))))))

(deftest unit-test-user-gets-a-tie
  (testing "test user gets a tie"
    (dotimes [_ 5](query/increment-users-current-streak! query/db {:username "Snake"}))
    (dotimes [_ 4](query/increment-users-current-streak! query/db {:username "Jarrod"}))
    (with-redefs [rand-nth        (fn [_] "paper")]
      (let [mention {:screen-name "Jarrod" :text "I choose paper"}
            result  (sut/response-to-mention mention)]
        (is (= "@Jarrod paper ties with paper no winner. Current streak: 4. The current champ has a streak of: 5" result))))))
