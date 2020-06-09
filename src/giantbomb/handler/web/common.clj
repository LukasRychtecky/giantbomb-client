(ns giantbomb.handler.web.common)

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
        "GiantBomb"]]]]
    body]])
