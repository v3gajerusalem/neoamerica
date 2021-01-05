(ns neoamerica.dev-journal
  (:require [hashp.core]
            [next.jdbc :as db]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as honeysql]
            [honeysql.helpers :as hh]
            [buddy.hashers :as hash]
            [reitit.core :as r]
            [reitit.ring :as reitit]
            [reitit.dev.pretty :as pretty]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [ring.adapter.jetty :as jetty]
            [malli.core :as malli]
            [integrant.core :as ig]
            [aero.core :as  aero]
            [rum.core :as rum]))




;;-- =[ BUDDY STUDY ]=
#_(def x (hash/encrypt "thisisapassword"))

#_(hash/check "thisisapassword" x)
;;-- =[ END BUDDY STUDY ]=

;; ** HANDLERS ***********************************************************
;; TODO authorization token?
;; TODO Sessions? How to handle this?
(defn auth-user [])


(defn respond-hello [request]
  {:status 200
   :body "NeoAmerica, ignite!"})
#p respond-hello

->>
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

    ["/create"
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
  (reitit/router routesds
                 {:data {:muuntaja m/instance
                         :middleware [swagger/swagger-feature
                                      muuntaja/format-middleware]}
                  :exception pretty/exception}))



(r/routes router)
;; ** END ROUTES *********************************************************

(def app
  (reitit/ring-handler router
                       (reitit/routes
                        (swagger-ui/create-swagger-ui-handler {:path "/"}))))

(defn start []
  (jetty/run-jetty #'app {:port 8000 :join false}))

#_ (start)



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
