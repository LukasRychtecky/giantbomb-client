(ns giantbomb.domain.cart.repository)

(defprotocol CartRepository
  (add [_ game])
  (checkout [_])
  (delete [_ guid]))
