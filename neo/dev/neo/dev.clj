(ns neo.dev
  (:require [hashp.core]
            [next.jdbc :as db]))

(def db-specification {:dbtype "postgresql" :dbname "neoamerica" :user "neo" :password "spiderdeus"})
(def data-source (db/get-datasource db-specification))

;; TODO: Data model for a post
(def Post
  {:id 1
   :author-id  1
   :post   "This will be the first post."
   :slug   "https-nice-slug-url"
   :image "should I store image in database"})
Post


;; TODO Comment Model. Will also need Users and authorization for them
(def Comment)



;; -- TABLE CREATION -----------------------------------
#_(db/execute!
   data-source
   ["create table author(
     id serial PRIMARY KEY,
     name VARCHAR(32),
     salt VARCHAR(50))"])

;; Note with h2, for now I'll grab the user "me" and physically just push the author id in
#_(db/execute!
 data-source
 ["create table posts(
   id serial PRIMARY KEY,
   author_id INT REFERENCES author (id),
   post text(400),
   slug varchar(200),
   image_loc varchar(150)

   ALTER TABLE posts
   ADD CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES customers (id)"])

;; -- DATABASE TRANSACTIONS -------------------------------------
#_(db/execute!
   data-source
   ["insert into author(name,salt)
   values('vegajerusalem','whoami')"])

#_(db/execute!
 data-source
 ["select * from author"])

#_(db/execute!
 data-source
 ["drop table author"])

;; /usr/lib64/postgresql-12/bin/pg_ctl -D /var/lib/postgresql/12/data -l logfile start
