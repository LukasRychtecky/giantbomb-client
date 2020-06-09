(ns giantbomb.domain.cart.service)

(defprotocol CartService
  (add [_ game]))
