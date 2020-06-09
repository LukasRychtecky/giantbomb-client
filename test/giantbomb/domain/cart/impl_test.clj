(ns giantbomb.domain.cart.impl-test
  (:require [clojure.test :as t]
            [giantbomb.domain.cart.fixtures :as fix]
            [giantbomb.domain.cart.impl :refer
             [->CartServiceImpl]]
            [giantbomb.domain.cart.repository :refer
             [CartRepository]]
            [giantbomb.domain.cart.service :as service]
            [giantbomb.domain.game.fixtures :as game-fix]
            [giantbomb.domain.game.service
             :refer [GameService]
             :as game-service]))

(t/deftest add-test
  (t/testing "should add a game"
    (let [cart fix/cart
          repository
          (reify CartRepository
            (add [_ game]
              (t/is (= game-fix/game game))
              cart))
          game-service
          (reify GameService
            (get-game [_ id]
              (t/is (= (:id game-fix/game) id))
              game-fix/game))
          service
          (->CartServiceImpl game-service repository)]
      (t/is (= cart
               (service/add service (:id game-fix/game))))))

  (t/testing "should not add a game, not found"
    (let [game-service
          (reify GameService
            (get-game [_ _]
              game-service/not-found-result))
          service
          (->CartServiceImpl game-service nil)]
      (t/is (= game-service/not-found-result
               (service/add service (:id game-fix/game))))))

  (t/testing "should not add a game, error when getting a game"
    (let [game-service
          (reify GameService
            (get-game [_ _]
              game-service/unexpected-error-result))
          service
          (->CartServiceImpl game-service nil)]
      (t/is (= game-service/unexpected-error-result
               (service/add service (:id game-fix/game)))))))

(t/deftest delete-test
  (let [expected-guid (:guid game-fix/game)
        cart fix/cart
        repository
        (reify CartRepository
          (delete [_ guid]
            (t/is (= expected-guid guid))
            cart))
        service
        (->CartServiceImpl nil repository)]
    (t/is (= cart
             (service/delete service expected-guid)))))

(t/deftest checkout-test
  (let [cart {:games []}
        repository
        (reify CartRepository
          (checkout [_]
            cart))
        service
        (->CartServiceImpl nil repository)]
    (t/is (= cart
             (service/checkout service)))))

(t/deftest get-cart-test
  (let [cart {:games []}
        repository
        (reify CartRepository
          (get-cart [_]
            cart))
        service
        (->CartServiceImpl nil repository)]
    (t/is (= cart
             (service/get-cart service)))))
