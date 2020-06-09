(ns giantbomb.domain.cart.service)

(defprotocol CartService
  (add [_ game])
  (checkout [_])
  (delete [_ guid])
  (get-cart [_]))
