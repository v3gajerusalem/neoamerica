(ns neoamerica.server.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-response]
            [io.pedestal.http.ring-middlewares :as middlewares]
            [hiccup.core :refer [html]]
            [hashp.core])
  (:import (org.eclipse.jetty.server Server)))


;; -- HTTP Utilities --------------------------------------------------
(defn response [status body & [headers :as hdrs]]
  {:status status
   :body body
   :headers (->>
             (if
                 (map? headers)
                 (mapcat (fn [[k v]] [(name k) (str v)]) headers)
                 hdrs)
             (apply hash-map))})

;; HTTP STATUS
(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))
(def error    (partial response 500))
(defn not-found []
  {:status 404 :body "Not found\n"})


;; -- Common Interceptors -----------------------------------------------
(def common-interceptors [(body-params/body-params) http/html-body])

;; -- Base Page ----------------------------------------------------------
#p (defn page [request]
  (ok
   (html
    [:html
     {:lang "en"}
     [:head
      [:meta {:charset "UTF-8"}]
      [:title "Neo America"]]
     [:body
      [:div "Neo America"]]])))
#_(route/try-routing-for neoamerica.scratch/routes :prefix-tree "/" :get)
;; -- Routes -----------------------------------------------------------
(def routes
  ;; ["/echo" :get respond-hello :route-name echo]
  (route/expand-routes
   #{["/" :get (conj common-interceptors `page)]}))


;; -- Dev Server Storyline --------------------------------------------
(defonce server-dev (atom nil))

(def service-map
  { ;; ::http/host  "0.0.0.0"
   ;; ::http/allowed-origins {:allowed-origins (fn[_] true) :creds true}
   ::http/type   :jetty
   ::http/routes #(deref #'routes)
   ::http/resource-path "/public"
   ;; ::http/container-options
   ;; {:context-configurator server-ws/websocket-configurator-for-jetty
   ;;  :h2c? true
   ;;  :h2 true
   ;;  :ssl? true
   ;;  :ssl-port server-config/server-ssl-port
   ;;  :keystore server-config/keystore-location
   ;;  :key-password server-config/keystore-password
   ;;  :security-provider "Conscrypt"}
   ::http/port   8080})

(defn server-can-be-started?
  []
  (or
   (nil? (-> server-dev deref))
   (.isStopped
    ^Server
    (-> server-dev deref :io.pedestal.http/server))))

(defn start-dev []
  (if-not
      (server-can-be-started?)
      (printlnds "start-dev requested but Jetty server is already running.")
      (reset! server-dev
              (http/start
               (http/create-server
                (-> service-map
                    (assoc
                     ::http/join? false)))))))

(defn stop-dev []
  (http/stop @server-dev))





(defn restart-dev []
  (stop-dev)
  (start-dev))

;; dev-control
#_(start-dev)
#_(stop-dev)
#_(restart_dev)
