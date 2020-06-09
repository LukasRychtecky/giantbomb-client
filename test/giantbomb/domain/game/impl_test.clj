(ns giantbomb.domain.game.impl-test
  (:require [clojure.test :as t]
            [giantbomb.domain.game.fixtures :as fix]
            [giantbomb.domain.game.impl :refer
             [->GameServiceImpl]]
            [giantbomb.domain.game.repository :refer
             [GameRepository]]
            [giantbomb.domain.game.service :as service]))

(t/deftest find-all-test
  (t/testing "should return games"
    (let [games [fix/game]
          expected-query fix/query
          repository
          (reify GameRepository
            (find-all [_ query]
              (t/is (= expected-query query))
              games))
          service
          (->GameServiceImpl repository)]
      (t/is (= games
               (service/find-all service fix/query)))))

  (t/testing "should return an error"
    (let [repository
          (reify GameRepository
            (find-all [_ _]
              service/unexpected-error-result))
          service
          (->GameServiceImpl repository)]
      (t/is (= service/unexpected-error-result
               (service/find-all service fix/query))))))
