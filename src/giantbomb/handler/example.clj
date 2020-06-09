(ns giantbomb.handler.example
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [integrant.core :as ig]))

(defmethod ig/init-key :giantbomb.handler/example [_ options]
  (context "/example" []
    (GET "/" []
      (io/resource "giantbomb/handler/example/example.html"))))
