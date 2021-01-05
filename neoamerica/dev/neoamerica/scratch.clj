(ns neoamerica.scratch
  (:require [hashp.core]
            [next.jdbc :as db]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-response]
            [io.pedestal.http.ring-middlewares :as middlewares]
            [hiccup.core :refer [html]])
  (:import (org.eclipse.jetty.server Server)))

;; DONE Pedestal/Jetty Server
;; TODO Register route connect to your db/salt code...time to clean that up

(defn response [status body & [headers :as hdrs]]
  {:status status
   :body body
   :headers (->>
              (if
                (map? headers)
                (mapcat (fn [[k v]] [(name k) (str v)]) headers)
                hdrs)
              (apply hash-map))})

;; | HTTP Statuses

(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))
(def error    (partial response 500))
(defn not-found []
  {:status 404 :body "Not found\n"})







(def echo
  {:name ::echo
   :enter (fn [context]
            (let [request (:request context)
                  response (ok request "Content-Type" "text/plain")]
              (assoc context :response response)))})


(defn respond-hello [request]
  {:status 200 :body "hello"})


(def common-interceptors [(body-params/body-params) http/html-body])
(defn page [request]
  (ring-response/response
   (html
    [:html
     {:lang "en"}
     [:head
      [:meta {:charset "UTF-8"}]
      [:title "Neo America"]]
     [:body
      [:div "Neo America"]]]))
  ;; "Content-Type"
  ;; "text/html"
  )

(defn page2 [request]
  {:status 200 :body (str "Hello, Neo America")})
(def supported-types
  ["text/html" "application/edn"
   "application/json" "text/plain"
   "application/transit+json" "text/event-stream"])

(def routes
  ;; ["/echo" :get respond-hello :route-name echo]
  (route/expand-routes
   #{["/" :get (conj common-interceptors `page)]})
  )


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

(defn stop-dev []
  (http/stop @server-dev))

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
      (println "start-dev requested but Jetty server is already running.")
      (reset! server-dev
              (http/start
               (http/create-server
                (-> service-map
                    (assoc
                     ::http/join? false)))))))



#_(start-dev)
#_(stop-dev)

(defn restart-dev []
  (stop-dev)
  (start-dev))
#_(restart_dev)


;; Play area
#_(route/try-routing-for neoamerica.scratch/routes :prefix-tree "/landing" :get)













;;  Integrant
#_(def system-config
    {::a {:b (ig/ref ::b)}
     ::b {:c (ig/ref ::c)}
     ::c nil})

#_(defmethod ig/init-key ::a [a {:keys [b]}]
    (println "Initialize " a))


#_(defmethod ig/init-key ::b [b {:keys [c]}]
    (println "Initialize " b))

#_(defmethod ig/init-key ::c [c {:keys [c _]}]
  (println "Initialize c" c))


#_(defmethod ig/halt-key! ::a [_ a]
  (println "Halt" a))

#_(defmethod ig/halt-key! ::b [_ b]
  (println "Halt" b))

#_(defmethod ig/halt-key! ::c [_ c]
  (println "Halt" c))

;;  -- Initialize and Halt System
#_(def system (ig/init system-config))
#_(ig/halt! system)
