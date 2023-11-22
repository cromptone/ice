(ns ice.artist-metadata.web.controllers.artist-of-day
  (:require
   [clojure.tools.logging :as log]
   [ring.util.http-response :as http-response]))

(defn fetch-artist-of-day! [{:keys [query-fn] :as opts}
                            {{{date :date} :query} :parameters}]
  (let [parsed-date (java.time.LocalDate/parse date (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd"))]
    (if-let [artist-of-day (query-fn :fetch-artist-of-day! {:date parsed-date})]
      (http-response/ok artist-of-day)
      ;; Not a great solution; not RESTful.
      (do (query-fn :set-artist-of-the-day-for-three-days! {})
          (http-response/ok (query-fn :fetch-artist-of-day! {:date parsed-date}))))))
          