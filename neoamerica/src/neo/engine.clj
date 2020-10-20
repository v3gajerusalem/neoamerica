(ns neo.engine
  (:require [io.pedestal.http :as http]
            [clojure.stacktrace :as stacktrace]
            [hashp.core]))

;; TODO Create a post with a limit of 3000 characters
;; TODO Take title, text, upload an image and create a slug
;; -- Create Post ------------------------------------------------------
#_(defn create-post [req])
;; Post should start in draft mode then I have the ability to `Launch` it
#_(defn delete-post [req])



;; (defn respond-hello [request]
;;   {:status 200
;;    :body "NeoAmerica, ignite!"})

;; (def routes
;;   #{["/hello" :get `respond-hello]})

 

;; (def service-map
;;   {::http/routes routes
;;    ::http/type :jetty
;;    ::http/port 8000})

;; (defn start []
;;   (http/start (http/create-server service-map)))


;; ;; For interactive development
;; (defonce server (atom nil))

;; (defn start-dev []
;;   (reset! server
;;           (http/start (http/create-server
;;                        (assoc service-map
;;                               ::http/join? false)))))

;; (defn stop-dev []
;;   (http/stop @server))

;; (defn restart []
;;   (stop-dev)
;;   (start-dev))
