(ns giantbomb.services-test
  (:require [clojure.test :as t]
            [giantbomb.domain.cart.impl :as cart]
            [giantbomb.domain.cart.repository :refer
             [CartRepository]]
            [giantbomb.domain.game.impl :as game]
            [giantbomb.domain.game.repository :refer
             [GameRepository]]
            [giantbomb.domain.game.service :refer
             [GameService]]
            [giantbomb.services :as services]
            [integrant.core :as ig]))

(t/deftest cart-service-test
  (t/testing "should create a Cart Service"
    (let [repository
          (reify CartRepository
            (add [_ _])
            (checkout [_])
            (delete [_ _]))
          game-service
          (reify GameService
            (find-all [_ _])
            (get-game [_ _]))]
      (t/is (= (cart/->CartServiceImpl game-service repository)
               (ig/init-key ::services/cart-service
                            {:game-service game-service
                             :repository repository}))))))

(t/deftest game-service-test
  (t/testing "should create a Game Service"
    (let [repository
          (reify GameRepository
            (find-all [_ _])
            (get-game [_ _]))]
      (t/is (= (game/->GameServiceImpl repository)
               (ig/init-key ::services/game-service
                            {:repository repository}))))))
