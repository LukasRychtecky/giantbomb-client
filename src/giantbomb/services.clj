(ns giantbomb.services
  (:require [integrant.core :as ig]
            [giantbomb.domain.game.impl :as game]))

(defmethod ig/init-key ::game-service
  [_ opts]
  (game/map->GameServiceImpl opts))
