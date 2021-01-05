(ns neoamerica.system
  (:require [clojure.pprint]
            ;; [reitit.core :as r]
            ;; [reitit.ring :as reitit]
            ;; [reitit.swagger :as swagger]
            ;; [reitit.swagger-ui :as swagger-ui]
            ;; [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [neoamerica.service :as service]))


;; -- =[SYSTEM CONFIGURATION]= ------------------------------------------------------
(def system-config 
  {:neoamerica/jetty {:handler (ig/ref :neoamerica/handler) :port 8000}
   :neoamerica/handler nil})            ;; TODO add hikari-cp connection pool


;; -- =[INITIALIZE WEBSERVER]= -------------------------------------------------------
(defmethod ig/init-key :neoamerica/jetty [handler {:keys [handler port]}]
     #p (println "\n*** *** =[NeoAmerica Server starting at port: ]=" port "***")
     (jetty/run-jetty handler  {:port port :join? false}))


;; -- =[INITIALIZE HANDLER]= ---------------------------------------------------------
(defmethod ig/init-key :neoamerica/handler [handler _]
  #p (println "\n*** *** *** [Starting NeoAmerica handlers] ***")
  (service/create-app))


;; -- =[HALT WEBSERVER]= -------------------------------------------------------------
(defmethod ig/halt-key! :neoamerica/jetty [_ jetty]
  #p (println "*** *** *** [Stopping NeoAmerica Web Server] ***")
  (.stop jetty))


(comment
  (def sys (ig/init system-config))
  (ig/halt! sys)
  )


;; TODO [Main entry point]
#_(defn -main [& args] 
    (ig/init _))
