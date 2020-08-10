(ns neo.db
  (:require [next.jdbc :as db
             hashp.core]))

;; -- Database Spec ------------------------------------
(def db-specification {:dbtype "postgresql"
                       :dbname "neoamerica"
                       :user "neo"
                       :password "spiderdeus"})

;; -- Data Source --------------------------------------
(def data-source (db/get-datasource db-specification))


;;  See dev.clj for table creation. Will figure out how to organize that later. I believe I want to include hug sql


