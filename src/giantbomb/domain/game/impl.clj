(ns giantbomb.domain.game.impl
  (:require [giantbomb.domain.game.repository :as repository]
            [giantbomb.domain.game.service :refer
             [GameService]]))

(defrecord GameServiceImpl [repository]
  GameService
  (find-all [_ query]
    (repository/find-all repository query))
  (get-game [_ guid]
    (repository/get-game repository guid)))
