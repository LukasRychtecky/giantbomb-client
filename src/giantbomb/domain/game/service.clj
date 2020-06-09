(ns giantbomb.domain.game.service
  (:require [giantbomb.domain.game.dto :as dto]))

(defprotocol GameService
  (find-all [_ query])
  (get-game [_ guid]))

(def unexpected-error-result {:error ::dto/unexpected-error})

(def not-found-result {:error ::dto/not-found})
