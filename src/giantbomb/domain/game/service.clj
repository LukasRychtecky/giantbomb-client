(ns giantbomb.domain.game.service
  (:require [giantbomb.domain.game.dto :as dto]))

(defprotocol GameService
  (find-all [_ query]))

(def unexpected-error-result {:error ::dto/unexpected-error})
