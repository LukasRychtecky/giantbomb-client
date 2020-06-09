(ns giantbomb.services
  (:require [giantbomb.domain.cart.impl :as cart]
            [giantbomb.domain.game.impl :as game]
            [integrant.core :as ig]))

(defmethod ig/init-key ::cart-service
  [_ opts]
  (cart/map->CartServiceImpl opts))

(defmethod ig/init-key ::game-service
  [_ opts]
  (game/map->GameServiceImpl opts))
