(ns giantbomb.domain.game.dto
  (:require [clojure.spec.alpha :as s]))

(s/def ::guid string?)
(s/def ::id int?)
(s/def ::name string?)
(s/def ::thumb-url string?)

(s/def ::game
  (s/keys :req-un [::guid
                   ::id
                   ::name
                   ::thumb-url]))

(s/def ::query
  (s/keys :req-un [::name]))

(s/def ::not-found #{{:error ::not-found}})

(s/def ::unexpected-error #{{:error ::unexpected-error}})
