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


;; -- =[Author]= ------------------------------------------------------------

;; -- =[create-author]= --
(defn create-author [{:keys [handle email password]}]
  "Create authors"
  (let [salt (hash/encrypt password)
        created-author (-> (hh/insert-into :authors)
                           (hh/columns :handle :email :password)
                           (hh/values [[handle email salt]])
                           honeysql/format
                           db-query)
        sanitized-author (dissoc created-author :password)]
    sanitized-author))

#_(create-author {:handle   "v3gajerusalem"
                :email    "jb@email.com"
                :password "thisismeguy" })

(defn get-author [])                    ;; TODO Get Author
(defn get-all-authors [])               ;; TODO Get all Authors
(defn delete-author [])                 ;; TODO Delete Author


;; -- =[Author]= ------------------------------------------------------------

;; -- =[DATABASE TABLE CREATION]= --------------------------------------------------------

  (db/execute! ;;TODO Note with h2, for now I'll grab the user "me" and physically just push the author id in
   data-source
   ["create table posts(
   id serial PRIMARY KEY,
   author_id INT REFERENCES author (id),
   title VARCHAR(100),
   slug VARCHAR(200),
   content TEXT(3001), ;; TODO Drop table and recreate database
   views INTEGER,
   image_loc VARCHAR(150)

   ALTER TABLE posts
   ADD CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES customers (id)"])
  


