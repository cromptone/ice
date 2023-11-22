(ns ice.artist-metadata.tasks.handler
  (:require
   [clojure.tools.logging :as log]
   [ice.artist-metadata.web.middleware.core :as middleware]
   [integrant.core :as ig]
   [ring.util.http-response :as http-response]
   [reitit.ring :as ring]
   [reitit.swagger-ui :as swagger-ui])
  (:import (org.quartz Job)))


(defmethod ig/init-key :artist-metadata.task/update-artist-of-day
  [_ config]
  (let [query-fn (:query-fn config)]
    (reify Job
      (execute [this job-context]
        (->>
         (query-fn :set-artist-of-the-day-for-three-days! {})
         first
         (log/info "artist-metadata.task/update-artist-of-day:"))))))