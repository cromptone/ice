(ns ice.artist-metadata.env
  (:require
    [clojure.tools.logging :as log]
    [ice.artist-metadata.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[artist-metadata starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[artist-metadata started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[artist-metadata has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
