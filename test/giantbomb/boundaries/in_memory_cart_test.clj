(ns giantbomb.boundaries.in-memory-cart-test
  (:require [clojure.test :as t]
            [giantbomb.boundaries.in-memory-cart
             :refer [->InMemoryCart]
             :as in-memory-cart]
            [giantbomb.domain.game.fixtures :as fix]
            [giantbomb.domain.cart.repository :as repository]
            [integrant.core :as ig]))

(t/deftest in-memory-cart-test-test
  (let [cart (->InMemoryCart (atom {:games []}))
        game fix/game]
    (t/testing "should add a game"
      (t/is (= {:games [game]}
               (repository/add cart game))))

    (t/testing "should not add same game twice"
      (t/is (= {:games [game]}
               (repository/add cart game))))

    (t/testing "should remove a game"
      (t/is (= {:games []}
               (repository/delete cart (:guid game)))))
    (t/testing "should clear a cart"
      (t/is (= {:games []}
               (repository/checkout cart))))
    (t/testing "should return a cart"
      (t/is (= {:games []}
               (repository/get-cart cart))))))

(t/deftest init-key-cart-test
  (let [*cart ::custom-atom]
    (t/is (= (->InMemoryCart *cart)
             (ig/init-key ::in-memory-cart/cart {:*cart *cart})))))
