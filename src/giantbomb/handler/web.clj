(ns giantbomb.handler.web
  (:require [compojure.core :as compojure]
            [giantbomb.handler.web.search :as search]
            [hiccup.core :as hiccup]
            [integrant.core :as ig]
            [ring.util.response :as ring]))

(def headers {"Accept" "text/html"
              "Content-Type" "text/html"})

(def html-response (comp #(assoc % :headers headers) ring/response))

(defmethod ig/init-key :giantbomb.handler/web
  [_ {:keys [game-service]}]
  (compojure/GET "/"
                 [:as req]
                 (-> game-service
                     (search/search-page req)
                     hiccup/html
                     html-response)))
