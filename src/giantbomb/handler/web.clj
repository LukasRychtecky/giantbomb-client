(ns giantbomb.handler.web
  (:require [compojure.core :as compojure]
            [giantbomb.handler.web.search :as search]
            [giantbomb.handler.web.cart :as cart]
            [hiccup.core :as hiccup]
            [integrant.core :as ig]
            [ring.util.response :as ring]))

(def headers {"Accept" "text/html"
              "Content-Type" "text/html"})

(def html-response (comp #(assoc % :headers headers) ring/response))

(defmethod ig/init-key :giantbomb.handler/web
  [_ {:keys [cart-service game-service]}]
  (compojure/context
   "/"
   []
   (compojure/GET "/"
                  [:as req]
                  (-> cart-service
                      (search/search-page game-service req)
                      hiccup/html
                      html-response))
   (compojure/POST "/cart-add-game"
                   [:as req]
                   (cart/add-game cart-service req))
   (compojure/POST "/cart-delete-game"
                   [:as req]
                   (cart/delete-game cart-service req))
   (compojure/GET "/checkout"
                  []
                  (-> cart-service
                      cart/checkout-page
                      hiccup/html
                      html-response))
   (compojure/POST "/checkout"
                  []
                  (cart/checkout cart-service))))
