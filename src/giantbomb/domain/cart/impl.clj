(ns giantbomb.domain.cart.impl
  (:require [cats.core :as m]
            [cats.monad.either :as either]
            [giantbomb.domain.cart.repository :as repository]
            [giantbomb.domain.cart.service :refer
             [CartService]]
            [giantbomb.domain.game.service :as game-service]))

(defn- get-game
  [game-service id]
  (let [result (game-service/get-game game-service id)]
    (condp = result
      game-service/not-found-result
      (either/left result)

      game-service/unexpected-error-result
      (either/left result)

      (either/right result))))

(defrecord CartServiceImpl [game-service repository]
  CartService
  (add [_ id]
    (m/extract (m/>>= (get-game game-service id)
                      (comp either/right (partial repository/add repository)))))

  (checkout [_]
    (repository/checkout repository))

  (delete [_ guid]
    (repository/delete repository guid))

  (get-cart [_]
    (repository/get-cart repository)))
