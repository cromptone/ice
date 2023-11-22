(ns ice.artist-metadata.web.routes.utils 
  (:require [clojure.string :as string]))

(def route-data-path [:reitit.core/match :data])

(defn route-data
  [req]
  (get-in req route-data-path))

(defn route-data-key
  [req k]
  (get-in req (conj route-data-path k)))

(defn uuid-or-uuid-string? [s]
  (or (uuid? s)
      (and (string? s)
           (-> s
               parse-uuid
               uuid?))))

(defn yyyy-MM-dd-str? [s]
  (try (java.time.LocalDate/parse s (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd"))
       true
       (catch Exception _e
         false)))

(defn string-valid? [s]
  (and (string? s)
       (not (string/blank? s))
       (= (string/trim s) s)))