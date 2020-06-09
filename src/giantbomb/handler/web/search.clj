(ns giantbomb.handler.web.search
  (:require [clojure.string :as string]
            [giantbomb.domain.cart.service :as cart-service]
            [giantbomb.domain.game.service :as game-service]
            [giantbomb.handler.web.common :as common]
            [ring.middleware.anti-forgery :as anti-forgery]))

(defn- csrf-field
  []
  [:input {:name "__anti-forgery-token"
           :type "hidden"
           :value anti-forgery/*anti-forgery-token*}])

(defn- search-form
  [query]
  [:form {:action "/", :method "get"}
   [:input {:class "form-control"
            :name "name"
            :type "text"
            :value (:name query)}]
   [:button {:class "btn btn-primary"
             :type "submit"}
    "Search"]])

(def ^:private compose-add-url
  (partial common/compose-url "/cart-add-game"))

(def ^:private compose-delete-url
  (partial common/compose-url "/cart-delete-game"))

(defn- action-buttons
  [query in-cart? game]
  (if (in-cart? (:guid game))
    [:form {:action (compose-delete-url query), :method "post"}
     (csrf-field)
     [:input {:name "guid", :type "hidden", :value (:guid game)}]
     [:button {:class "btn btn-lg btn-block btn-outline-primary"}
      "Remove from cart"]]
    [:form {:action (compose-add-url query), :method "post"}
     (csrf-field)
     [:input {:name "guid", :type "hidden", :value (:guid game)}]
     [:button {:class "btn btn-lg btn-block btn-primary"}
      "Add to cart"]]))

(defn card-game
  [query in-cart? game]
  [:div {:class "card mb-4 shadow-sm"
         :id (format "game-%s" (:id game))}
   [:img {:class "card-img-top"
          :src (:thumb-url game)}]
   [:div.card-body
    [:div.card-title
     (:name game)]
    (action-buttons query in-cart? game)]])

(defn- search-result
  [cart-service game-service query]
  (let [result (when query
                 (game-service/find-all game-service query))
        cart (cart-service/get-cart cart-service)
        in-cart? (partial contains? (->> cart :games (map :guid) set))]
    [:div.album.py-5.bg-light
     [:div.container
      (cond
        (nil? result)
        nil

        (= result game-service/unexpected-error-result)
        [:div.alert.alert-danger
         "GiantBomb API is not available"]

        (empty? result)
        [:div.alert.alert-info "No games found"]

        :else
        [:div.row
         (for [game result]
           [:div.col-md-4
            (card-game query in-cart? game)])])]]))

(defn search-page
  [cart-service game-service req]
  (let [query (common/req->query req)
        main
        [:main {:role "main"}
         [:section.jumbotron.text-center
          [:div.container
           [:h1 "Search games"]
           (search-form query)]]
         (search-result cart-service game-service query)]]
    (common/layout main)))
