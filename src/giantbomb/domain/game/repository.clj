(ns giantbomb.domain.game.repository)

(defprotocol GameRepository
  (find-all [_ query])
  (get-game [_ guid]))
