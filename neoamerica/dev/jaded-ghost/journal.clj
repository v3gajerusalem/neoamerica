(ns jaded-ghost.journal
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]
            [clojure.java.io]
            [clojure.string]
            [hashp.core]))

;; MOJO meets minority report meets blade runner

;; TODO Politicians from all branches
(def branch #{:legislative :executive :judicial})
branch

;; Legislative rules
;; Senate, Congress, Congressional Budget Office
;; -- US Senate & House of Representatives
;; -- -- Senators come in Class I - III -- https://en.wikipedia.org/wiki/Classes_of_United_States_senators
;; -- -- Classes I-II hold 33 seats/ Class III holds 34
;; -- -- Jr/Senior roles exist

(def legislative-branch {:us-senate {}
                         :hor {}})
;; Executive rules
;; Judicial rules

(defrecord politician [id first-name last-name branch party state position email  age image])
politician



;; https://www.propublica.org/d - fGP6pB50eqy6tliOnDOCx44l3wsKCFAxMzDYTEKs
;; https://projects.propublica.org/api-docs/congress-api/
;; https://api.propublica.org/congress/v1

;; curl "https://api.propublica.org/congress/v1/116/senate/members.json" -H "X-API-Key:  fGP6pB50eqy6tliOnDOCx44l3wsKCFAxMzDYTEKs"

;; (def politicians {:first-name :last-name :state :city :party :elected-when? :picture-url :position})

(def senate-api "fGP6pB50eqy6tliOnDOCx44l3wsKCFAxMzDYTEKs")
;; - Figuring senatet data out here
(def senate-data
  (:body (client/get "https://api.propublica.org/congress/v1/116/senate/members.json" {:headers {"X-Api-Key" senate-api} :as :json})))

#p senate-data
(take 30 senate-data)
(keys senate-data)

(get-in senate-data [:results :congress])
(-> senate-data
    :results
    identity)


(spit "/Users/akuma/Code/neoamerica/resources/senate.json" senate-data) ;; - Put json data in a file to see structure
;; Now to figure out how to get the results out of :results

(get-in senate-data [:results :num_results]) ;; nil
(get (get-in [:results :num_results]) 6)
(get-in senate-data [:results ])
(->> senate-data :results (map :chamber)) ;; pulls "Senate" from chamber, good
(->> senate-data :results (map #(->> % :members (map :first_name)))) ;; all names
(->> senate-data :results (mapcat #(->> % :members (map :first_name))))

(defn get-senator-data
  "Fetch necessary fields from senate-data json feed"
  [data]
  (let [members (->> data :results (mapcat :members))
        first-name (map :first_name members)
        last-name (map :last_name members)]
    ;; (clojure.string/trim-newline (println-str (first first-name) (first last-name)))
    ))

(get-senator-data senate-data)
;; (map #(select-keys % your-keys) ...)


;; ^ now I can take this and create a function that goes through and gets every field I want. after that...store it to a database
(let [members (->> senate-data :results (mapcat :members))]
  (->> members (mapcat :first_name)))

#_(def data-fields [:first_name :last_name :middle_name
                  :date_of_birth :gender :next_election
                  :party :state_rank :state :in_office
                  :seniority :leadership_role :title
                  :total_votes :votes_with_party_pct
                  :missed_votes :phone :office :contact_form])

