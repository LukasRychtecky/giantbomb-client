(ns giantbomb.domain.cart.repository)

(defprotocol CartRepository
  (add [_ game]))
