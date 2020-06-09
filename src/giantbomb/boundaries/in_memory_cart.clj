(ns giantbomb.boundaries.in-memory-cart
  (:require [giantbomb.domain.cart.repository :refer
             [CartRepository]]
            [integrant.core :as ig]))

(defn- add-game
  [cart game]
  (if (some #(when (= (:guid %) (:guid game)) %) (:games cart))
    cart
    (update cart :games conj game)))

(defn- delete-game
  [cart guid]
  (update cart :games (partial remove #(= (:guid %) guid))))

(defrecord InMemoryCart [*cart]
  CartRepository
  (add [_ game]
    (swap! *cart add-game game))
  (checkout [_]
    (reset! *cart {:games []}))
  (delete [_ guid]
    (swap! *cart delete-game guid))
  (get-cart [_]
    @*cart))

(defmethod ig/init-key ::cart
  [_ opts]
  (map->InMemoryCart (merge {:*cart (atom {:games []})}
                            opts)))
