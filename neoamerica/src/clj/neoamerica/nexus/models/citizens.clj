(ns neoamerica.nexus.models.citizens)

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

;; -- =[Author SQL STUFF]= ------------------------------------------------------------
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
