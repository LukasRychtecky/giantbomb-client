(ns giantbomb.domain.cart.fixtures
  (:require [clojure.spec.alpha :as s]
            [giantbomb.domain.cart.dto :as dto]
            [giantbomb.domain.game.fixtures :as fix]))

(s/check-asserts true)

(def cart
  (s/assert ::dto/cart
            {:games [fix/game]}))
