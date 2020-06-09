(ns giantbomb.services-test
  (:require [clojure.test :as t]
            [giantbomb.domain.game.impl :as game]
            [giantbomb.domain.game.repository :refer
             [GameRepository]]
            [giantbomb.services :as services]
            [integrant.core :as ig]))

(t/deftest game-service-test
  (t/testing "should create a Game Service"
    (let [repository
          (reify GameRepository
            (find-all [_ _]
              []))]
      (t/is (= (game/->GameServiceImpl repository)
               (ig/init-key ::services/game-service
                            {:repository repository}))))))
