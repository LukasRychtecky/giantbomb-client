(ns giantbomb.handler.web.common
  (:require [clojure.string :as string]
            [ring.middleware.anti-forgery :as anti-forgery]))

(defn csrf-field
  []
  [:input {:name "__anti-forgery-token"
           :type "hidden"
           :value anti-forgery/*anti-forgery-token*}])

(defn req->query
  [req]
  (let [name-param (-> req :params :name)]
    (when-not (string/blank? name-param)
      {:name name-param})))

(defn compose-url
  [path query]
  (if-not (-> query :name string/blank?)
    (format "%s?name=%s" path (:name query))
    path))

(defn- headers
  []
  [:head
   [:link {:crossorigin "anonymous"
           :href "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
           :integrity "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
           :rel "stylesheet"}]])

(defn layout
  [body]
  [:html
   (headers)
   [:body
    [:header
     [:div.navbar.navbar-dark.bg-dark.shadow-sm
      [:div.container.d-flex.justify-content-between
       [:a {:class "navbar-brand d-flex align-items-center"
            :href "/"}
        "GiantBomb"]
       [:nav
        [:a {:class "py-2 d-none d-md-inline-block"
             :href "/checkout"}
         "Checkout"]]]]]
    body]])
