(ns neo.scratch)

(comment
  (ns neo.dev
  (:require [hashp.core]
            [next.jdbc :as db]
            [io.pedestal.http :as http]
            [reitit.ring :as reitit]
            [integrant.core :as ig]
            [aero.core :as  aero])))

x
#_(def system-config
    {::a {:b (ig/ref ::b)}
     ::b {:c (ig/ref ::c)}
     ::c nil})

#_(defmethod ig/init-key ::a [a {:keys [b]}]
  (println "Initialize " a))

#_(defmethod ig/init-key ::b [b {:keys [c]}]
  (println "Initialize " b))

#_(defmethod ig/init-key ::c [c {:keys [c _]}]
  (println "Initialize c" c))


#_(defmethod ig/halt-key! ::a [_ a]
  (println "Halt" a))

#_(defmethod ig/halt-key! ::b [_ b]
  (println "Halt" b))

#_(defmethod ig/halt-key! ::c [_ c]
  (println "Halt" c))

;;  -- Initialize and Halt System
#_(def system (ig/init system-config))
#_(ig/halt! system)
