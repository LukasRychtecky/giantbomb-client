(ns giantbomb.handler.web.cart
  (:require [giantbomb.domain.cart.service :as service]
            [giantbomb.handler.web.common :as common]
            [ring.util.response :as ring]))

(defn add-game
  [service req]
  (service/add service (-> req :params :guid))
  (-> "/"
      (common/compose-url (common/req->query req))
      ring/redirect))

(defn delete-game
  [service req]
  (service/delete service (-> req :params :guid))
  (-> "/"
      (common/compose-url (common/req->query req))
      ring/redirect))

(defn checkout-page
  [service]
  (let [cart (service/get-cart service)]
    (common/layout
     [:main {:role "main"}
      [:section.jumbotron.text-center
       [:div.container
        [:h1 "Checkout"]
        [:ul.list-group
         (for [game (:games cart)]
           [:li.list-group-item (:name game)])]
        (if (-> cart :games seq)
          [:form {:action "/checkout", :method "post"}
           (common/csrf-field)
           [:button {:class "btn btn-lg btn-block btn-primary"}
            "Rent them all"]]
          [:a {:class "btn btn-primary"
               :href "/"}
           "Select games and rent them"])]]])))

(defn checkout
  [service]
  (service/checkout service)
  (ring/redirect "/"))
