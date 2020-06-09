(ns giantbomb.boundaries.giantbomb
  (:require [duct.logger :as logger]
            [giantbomb.domain.game.repository :refer
             [GameRepository]]
            [giantbomb.domain.game.service :as service]
            [integrant.core :as ig]
            [jsonista.core :as json]
            [org.httpkit.client :as http])
  (:import [com.fasterxml.jackson.core JsonParseException]))

(def ^:private default-headers
  {"Accept" "application/json"
   "Content-Type" "application/json"})

(def token "b93f3998e8095edd4b4cde0910470c78b6a13fc4")

(def ^:private decode
  #(json/read-value % (json/object-mapper {:decode-key-fn true})))

(defn- query-params
  [query token]
  {:api_key token
   :format "json"
   :query query})

(defn- ->game
  [obj]
  {:id (:id obj)
   :name (:name obj)
   :thumb-url (-> obj :image :thumb_url)})

(defrecord Giantbomb [host logger token]
  GameRepository
  (find-all [_ query]
    (let [url (format "%s/api/search" host)
          {:keys [body status] :as response}
          @(http/get url {:headers default-headers
                          :as :text
                          :query-params
                          (query-params (:name query) token)})]
      (try
        (case status
          200 (->> body decode :results (map ->game))
          service/unexpected-error-result)
        (catch JsonParseException e
          (logger/log logger :error {:exception e
                                     :response response})
          service/unexpected-error-result)))))

(defmethod ig/init-key ::connector
  [_ opts]
  (map->Giantbomb opts))
