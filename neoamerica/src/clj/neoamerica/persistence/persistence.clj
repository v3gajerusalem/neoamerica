(ns neoamerica.persistence.persistence)

(ns neoamerica.db
  (:require [hashp.core]
            [next.jdbc :as db]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as honeysql]
            [honeysql.helpers :as hh]
            [buddy.hashers :as hash]))

;; TODO Change over to hikari-cp for `db pool connection`
;; TODO Use environment variables to get unsafe info out of code base
;; TODO Migrations

;; TODO User log in
;; TODO User log out
;; 
;; -- =[DATABASE CONFIGURATION]= --------------------------------------------
(def db-specification {:dbtype   "postgresql"
                       :dbname   "neoamerica"
                       :user     "neo"
                       :password "spiderdeus"})


;; -- =[DATASOURCE]= --------------------------------------------------------
(def data-source (db/get-datasource db-specification))


;; -- =[PERSISTENCE FUNCTIONS]= ---------------------------------------------
(defn db-query [sql]
  (db/execute-one! data-source sql
                   {:return-keys true
                    :builder-fn rs/as-unqualified-maps}))


