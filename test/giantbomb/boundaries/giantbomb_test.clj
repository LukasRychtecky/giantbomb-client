(ns giantbomb.boundaries.giantbomb-test
  (:require [clojure.test :as t]
            [duct.logger :as logger]
            [giantbomb.boundaries.giantbomb :as giantbomb]
            [giantbomb.domain.game.fixtures :as fix]
            [giantbomb.domain.game.repository :as repository]
            [giantbomb.domain.game.service :as service]
            [jsonista.core :as json]
            [org.httpkit.fake :as http]
            [integrant.core :as ig]))

(def ^:private encode json/write-value-as-string)

(def ^:private host "https://www.giantbomb.com")

(def ^:private games-url "https://www.giantbomb.com/api/search")

(def ^:private game-url
  "https://www.giantbomb.com/api/game/3030-356")

(def ^:private token "1234")

(t/deftest find-all-test
  (t/testing "should return games"
    (let [game fix/game
          response
          {:body {:results
                  [{:guid (:guid game)
                    :id (:id game)
                    :image {:thumb_url (:thumb-url game)}
                    :name (:name game)}]
                  :status_code 1}
           :status 200}
          giantbomb (giantbomb/->Giantbomb host nil token)]
      (http/with-fake-http [games-url
                            (update response :body encode)]
        (t/is (= [fix/game]
                 (repository/find-all giantbomb fix/query))))))

  (t/testing "should return an unexpected error"
    (let [response
          {:body nil
           :status 500}
          giantbomb (giantbomb/->Giantbomb host nil token)]
      (http/with-fake-http [games-url
                            (update response :body encode)]
        (t/is (= service/unexpected-error-result
                 (repository/find-all giantbomb fix/query))))))

  (t/testing "should return an unexpected error, invalid json"
    (let [response
          {:body "{asdf"
           :status 200}
          logger
          (reify logger/Logger
            (-log [_ level _ _ _ _ event _]
              (t/is (= :error level))
              (t/is (= [:exception :response]
                       (keys event)))))
          giantbomb (giantbomb/->Giantbomb host logger token)]
      (http/with-fake-http [games-url response]
        (t/is (= service/unexpected-error-result
                 (repository/find-all giantbomb fix/query)))))))

(t/deftest get-game-test
  (t/testing "should return a game"
    (let [game fix/game
          response
          {:body {:results {:guid (:guid game)
                            :id (:id game)
                            :image
                            {:thumb_url (:thumb-url fix/game)}
                            :name (:name fix/game)}
                  :status_code 1}
           :status 200}
          logger
          (reify logger/Logger
            (-log [_ level _ _ _ _ event _]
              (t/is (= :error level))
              (t/is (= [:exception :response]
                       (keys event)))))
          giantbomb (giantbomb/->Giantbomb host logger token)]
      (http/with-fake-http [game-url
                            (update response :body encode)]
        (t/is (= game
                 (repository/get-game giantbomb (:guid game)))))))

  (t/testing "should return an error, a game not found"
    (let [response
          {:body {:status_code 101}
           :status 200}
          logger
          (reify logger/Logger
            (-log [_ level _ _ _ _ event _]
              (t/is (= :error level))
              (t/is (= [:exception :response]
                       (keys event)))))
          giantbomb (giantbomb/->Giantbomb host logger token)]
      (http/with-fake-http [game-url
                            (update response :body encode)]
        (t/is (= service/not-found-result
                 (repository/get-game giantbomb (:guid fix/game)))))))

  (t/testing "should return an error, an invalid json"
    (let [response
          {:body "{asdf"
           :status 200}
          logger
          (reify logger/Logger
            (-log [_ level _ _ _ _ event _]
              (t/is (= :error level))
              (t/is (= [:exception :response]
                       (keys event)))))
          giantbomb (giantbomb/->Giantbomb host logger token)]
      (http/with-fake-http [game-url response]
        (t/is (= service/unexpected-error-result
                 (repository/get-game giantbomb (:guid fix/game)))))))

  (t/testing "should return an error"
    (let [response
          {:body nil
           :status 500}
          logger
          (reify logger/Logger
            (-log [_ level _ _ _ _ event _]
              (t/is (= :error level))
              (t/is (= [:exception :response]
                       (keys event)))))
          giantbomb (giantbomb/->Giantbomb host logger token)]
      (http/with-fake-http [game-url response]
        (t/is (= service/unexpected-error-result
                 (repository/get-game giantbomb (:guid fix/game))))))))

(t/deftest init-key-connector
  (let [logger ::logger]
    (t/is (= (giantbomb/->Giantbomb host logger token)
             (ig/init-key ::giantbomb/connector {:host host
                                                 :logger logger
                                                 :token token})))))
