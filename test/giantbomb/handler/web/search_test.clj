(ns giantbomb.handler.web.search-test
  (:require [clojure.test :as t]
            [giantbomb.domain.cart.service :refer
             [CartService]]
            [giantbomb.domain.game.fixtures :as fix]
            [giantbomb.domain.game.service
             :refer [GameService]
             :as service]
            [giantbomb.handler.web]
            [integrant.core :as ig]
            [kerodon.impl :as kerodon]
            [net.cgrand.enlive-html :as enlive]
            [ring.mock.request :as mock]))

(defn- parse-body
  [body]
  (when body
    (kerodon/to-html-resource body)))

(defn- text
  [body selector]
  (-> body
      (enlive/select selector)
      first
      :content
      first))

(defn- search-api
  [conf & [{:keys [params]}]]
  (let [handler
        (ig/init-key :giantbomb.handler/web conf)]
    (-> (mock/request :get "/")
        (assoc :params params)
        handler
        (update :body parse-body))))

(t/deftest search-test
  (t/testing "should render a result without query"
    (let [conf
          {:cart-service
           (reify CartService
             (get-cart [_]
               {:games []}))}
          {:keys [status]} (search-api conf)]
      (t/is (= 200 status))))

  (t/testing "should render a result of a query"
    (let [expected-query {:name "silent"}
          conf
          {:cart-service
           (reify CartService
             (get-cart [_]
               {:games []}))
           :game-service
           (reify GameService
             (find-all [_ query]
               (t/is (= expected-query query))
               [fix/game]))}
          {:keys [body status]}
          (search-api conf {:params {:name "silent"}})]
      (t/is (= 200 status))
      (t/is (= (:name fix/game)
               (text body [:#game-356 :.card-title])))))

  (t/testing "should render a no result alert"
    (let [conf
          {:cart-service
           (reify CartService
             (get-cart [_]
               {:games []}))
           :game-service
           (reify GameService
             (find-all [_ _]
               []))}
          {:keys [body status]}
          (search-api conf {:params {:name "silent"}})]
      (t/is (= 200 status))
      (t/is (= "No games found"
               (text body [:.alert-info])))))

  (t/testing "should render a GiantBomb API not working"
    (let [conf
          {:cart-service
           (reify CartService
             (get-cart [_]
               {:games []}))
           :game-service
           (reify GameService
             (find-all [_ _]
               service/unexpected-error-result))}
          {:keys [body status]}
          (search-api conf {:params {:name "silent"}})]
      (t/is (= 200 status))
      (t/is (= "GiantBomb API is not available"
               (text body [:.alert-danger]))))))
