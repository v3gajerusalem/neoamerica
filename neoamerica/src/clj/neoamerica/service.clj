(ns neoamerica.service
  (:require [clojure.pprint]
            [neoamerica.utils :refer [wrap-jwt-authentication auth-middleware]]
            [reitit.core :as r]
            [reitit.ring :as reitit]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [rum.core :as rum]))


;; ** HANDLERS ***********************************************************
(defn respond-hello [request]
  {:status 200
   :body "NeoAmerica, ignite!"})
;; ** END HANDLERS *******************************************************

(def ok (constantly {:status 200 :body "ok"}))

;; ** ROUTES *************************************************************
#_(def routes
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title       "NeoAmerica API"
                            :description "NeoAmerica API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/users"
    {:swagger {:tags ["users"]}}

    ["/register"
     {:get  {:summary "Register User"
             :handler ok}
      :post {:summary "Register User"'
             :parameters {:body {:handle string?
                                 :password string?}}
             :handler ok}}]

    ["/create-user"
     {:get  {:summary "Create User"
             :handler ok}
      :post {:summary "Create User"
             :handler ok}}]

    ["/login"
     {:get  {:summary "User Login"
             :handler ok}
      :post {:summary "User Login"
             :handler ok}}]]])

(defn wizard [req]                      ;; TODO: determining if user exists here. at the moment the only user is me. going to work on this a bit. theres no order
  (let [ctx (:identity req)
        user (db/get-user ctx)]
    (if (nil? user)
      {:status 404
       :body {:error "Do you belong here?"}}
      {:status 200
       :body {:user user
              :token (create-token user)}})))

(def auth-routes
  [["/wizard-view" {:get {:middleware [wrap-jwt-authentication auth-middleware]
                          :handler wizard}}]])

(def router
  (reitit/router routes
                 {:data {:muuntaja m/instance
                         :middleware [swagger/swagger-feature
                                      muuntaja/format-middleware]}}))
;; END ROUTER STUFF HERE!



(defn create-app []
  (reitit/ring-handler router
                       (reitit/routes
                        (swagger-ui/create-swagger-ui-handler {:path "/swagger"}))))


