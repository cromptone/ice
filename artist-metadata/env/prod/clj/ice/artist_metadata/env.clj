(ns ice.artist-metadata.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[artist-metadata starting]=-"))
   :start      (fn []
                 (log/info "\n-=[artist-metadata started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[artist-metadata has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
