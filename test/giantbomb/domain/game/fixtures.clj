(ns giantbomb.domain.game.fixtures
  (:require [clojure.spec.alpha :as s]
            [giantbomb.domain.game.dto :as dto]))

(s/check-asserts true)

(def game
  (s/assert ::dto/game
            {:id 356
             :name "Silent Hill"
             :thumb-url "https://giantbomb1.cbsistatic.com/uploads/scale_avatar/6/62890/1538120-silent_hill_mobile3.jpg"}))

(def query
  (s/assert ::dto/query
            {:name "sillent"}))
