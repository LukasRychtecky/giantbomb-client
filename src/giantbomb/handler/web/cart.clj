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
