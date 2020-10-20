(ns neoamerica.nexus
  (:require [clojure.pprint]
            [reitit.core :as r]
            [reitit.ring :as reitit]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]))


;; ** HANDLERS ***********************************************************
(defn login [req]
  "User login handler
    [:handle, :password, :created-at, :last-login, :ip-addr]")

(defn respond-hello [request]
  {:status 200
   :body "NeoAmerica, ignite!"})
;; ** END HANDLERS *******************************************************

(def ok (constantly {:status 200 :body "ok"}))

;; ** ROUTES *************************************************************
(def routes
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


(def router
  (reitit/router routes
                 {:data {:muuntaja m/instance
                         :middleware [swagger/swagger-feature
                                      muuntaja/format-middleware]}}))
;; END ROUTER STUFF HERE!



(defn create-app []
  (reitit/ring-handler router
                       (reitit/routes
                        (swagger-ui/create-swagger-ui-handler {:path "/"}))))

#_(defn start []
  (jetty/run-jetty #'app {:port 8000 :join false}))

(def system-config 
  {:neoamerica/jetty {:handler (ig/ref :neoamerica/handler) :port 8000}
   :neoamerica/handler nil})


;; Initialize Jetty
(defmethod ig/init-key :neoamerica/jetty [handler {:keys [handler port]}]
     #p (println "NeoAmerica Server starting at port:" port)
     (jetty/run-jetty handler  {:port port :join? false}))

;; Initialize handler
(defmethod ig/init-key :neoamerica/handler [handler _]
  #p (println "*** *** *** [Starting NeoAmerica handlers] ***")
  (create-app))

(defmethod ig/halt-key! :neoamerica/jetty [_ jetty]
    #p (println "*** *** *** [Stopping NeoAmerica Web Server] ***")
  (.stop jetty))



(comment
  (defn start [])
  (def sys (ig/init system-config))
  (ig/halt! sys)
  )


;; TODO [Main entry point]
#_(defn -main [& args] 
    (ig/init _))


#_(def system-config {::c nil})



