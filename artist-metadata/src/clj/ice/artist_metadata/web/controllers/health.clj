(ns ice.artist-metadata.web.controllers.health
  (:require
   [ring.util.http-response :as http-response]
   [ice.artist-metadata.web.routes.utils :as utils])
  (:import
   [java.util Date]))

(defn healthcheck!
  [req]
  (let [{:keys [query-fn]} (utils/route-data req)]
    (http-response/ok
     {:time     (str (Date. (System/currentTimeMillis)))
      :up-since (str (Date. (.getStartTime (java.lang.management.ManagementFactory/getRuntimeMXBean))))
      :app      {:status  "up"
                 :message {:id (str query-fn "af939f")}}})))
