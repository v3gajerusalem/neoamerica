(ns neoamerica.utils
  (:require [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.sign.jwt :as jwt]))  ;; TODO Do yourself a favorit and take a look at buddy documentation. 


(def jwt-secret "JWT-SECRET")
(def backend (backends/jws (:secret jwt-secret)))

(defn wrap-jwt-authentication
  [handler]
  [wrap-authentication handler backend])

(defn auth-middleware
  [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body {:errorr "Unauthorized"}})))

(defn create-token [payload]
  (jwt/sign payload jwt-secret))

