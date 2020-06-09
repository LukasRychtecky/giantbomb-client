(ns giantbomb.handler.web.search
  (:require [clojure.string :as string]
            [giantbomb.domain.game.service :as service]
            [giantbomb.handler.web.common :as common]))

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

(defn card-game
  [game]
  [:div {:class "card mb-4 shadow-sm"
         :id (format "game-%s" (:id game))}
   [:img {:class "card-img-top"
          :src (:thumb-url game)}]
   [:div.card-body
    [:div.card-title
     (:name game)]]])

(defn- search-result
  [game-service query]
  (let [result (when query
                 (service/find-all game-service query))]
    [:div.album.py-5.bg-light
     [:div.container
      (cond
        (nil? result)
        nil

        (= result service/unexpected-error-result)
        [:div.alert.alert-danger
         "GiantBomb API is not available"]

        (empty? result)
        [:div.alert.alert-info "No games found"]

        :else
        [:div.row
         (for [game result]
           [:div.col-md-4
            (card-game game)])])]]))

(defn req->query
  [req]
  (let [name-param (-> req :params :name)]
    (when-not (string/blank? name-param)
      {:name name-param})))

(defn search-page
  [game-service req]
  (let [query (req->query req)
        main
        [:main {:role "main"}
         [:section.jumbotron.text-center
          [:div.container
           [:h1 "Search games"]
           (search-form query)]]
         (search-result game-service query)]]
    (common/layout main)))
