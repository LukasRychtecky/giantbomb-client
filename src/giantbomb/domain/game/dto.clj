(ns giantbomb.domain.game.dto
  (:require [clojure.spec.alpha :as s]))

(s/def ::id int?)
(s/def ::name string?)
(s/def ::thumb-url string?)

(s/def ::game
  (s/keys :req-un [::id
                   ::name
                   ::thumb-url]))

(s/def ::query
  (s/keys :req-un [::name]))

(s/def ::unexpected-error #{{:error ::unexpected-error}})
