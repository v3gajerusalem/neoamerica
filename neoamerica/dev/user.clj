(ns user
  (:require [next.jdbc :as db]
            [integrant.core :as ig]
            [integrant.repl :as repl :refer [clear go halt prep init reset reset-all]]
            [hashp.core]))

;; (def dev-config {:system
;;                  {:db {:dbtype "postgresql"
;;                        :dbname "neoamerica"}
;;                   :web/server {:env :dev
;;                                :io.pedestal.http/host "localhost"
;;                                :io.pedestal.http/port 8000
;;                                :io.pedestal.http/join? false
;;                                :io.pedestal.http/type :jetty}}})


;; (def dev-system-config)


;; (repl/set-prep! dev-config)
;; (repl/prep #p dev-config)

;; (integrant.repl/set-prep! (constantly {::foo {:example? true}}))
;; (prep)
;; (init)

;; (defn dev
;;   "Switch to 'dev' namespace"
;;   []
;;   (require 'dev)
;;   (in-ns 'dev))
