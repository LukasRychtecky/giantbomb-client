(ns giantbomb.domain.cart.dto
  (:require [clojure.spec.alpha :as s]
            [giantbomb.domain.game.dto :as dto]))

(s/def ::games
  (s/coll-of ::dto/game))

(s/def ::cart
  (s/keys :req-un [::games]))
