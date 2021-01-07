(ns neoamerica.dev-journal
  (:require [hashp.core]
            [next.jdbc :as db]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as honeysql]
            [honeysql.helpers :as hh]
            [buddy.hashers :as hash]

            [muuntaja.core :as m]

            [integrant.core :as ig]
            [aero.core :as  aero]))




;;-- =[ BUDDY STUDY ]=
#_(def x (hash/encrypt "thisisapassword"))

#_(hash/check "thisisapassword" x)
;;-- =[ END BUDDY STUDY ]=



;;-- =[ Database Stuff ]=

;;-- =[ DATABASE CONFIGURATION ]= 
;;-- Basic postgres connection
;;-- 
;;-- TODO Change over to hikari-cp for `db pool connection`
;;-- TODO Use environment variables to get unsafe info out of code base
(def db-specification {:dbtype   "postgresql"
                       :dbname   "neoamerica"
                       :user     "neo"
                       :password "spiderdeus"})

(def data-source (db/get-datasource db-specification))
;;-- =[ END DATABASE CONFIGURATION ]=


;; -- TABLE CREATION -----------------------------------
#_(db/execute!
   data-source
   ["create table authors(
     id serial PRIMARY KEY,
     handle VARCHAR(32),
     email VARCHAR(40),
     password VARCHAR(150))"])

#_(db/execute!
 data-source
 ["drop table authors"])


;;-- =[ DATABASE TRANSACTIONS ]=
(defn db-query-one [sql]
  (db/execute-one! data-source sql
                   {:return-keys true
                    :builder-fn rs/as-unqualified-maps}))

(defn create-author [{:keys [handle email password]}]
  (let [salt (hash/encrypt password)
        created-author (-> (hh/insert-into :authors)
                           (hh/columns :handle :email :password)
                           (hh/values [[handle email salt]])
                           honeysql/format
                           db-query-one)
        sanitized-author (dissoc created-author :password)]
    sanitized-author))

(create-author {:handle   "v3gajerusalem"
                :email    "jb@email.com"
                :password "thisismeguy" })
#_(db/execute!
   data-source
   ["insert into author(name,salt)
   values('vegajerusalem','whoami')"])

#_(db/execute!
 data-source
 ["select * from author"])

(db/execute!
 data-source
 ["drop table posts"])

;; /usr/lib64/postgresql-12/bin/pg_ctl -D /var/lib/postgresql/12/data -l logfile start








;;- =[NO MANS LAND!!!]=
;; Note with h2, for now I'll grab the user "me" and physically just push the author id in
#_(db/execute!
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
