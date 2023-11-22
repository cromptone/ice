(ns ice.artist-metadata.core
  (:require
   [clojure.tools.logging :as log]
   [integrant.core :as ig]
   [ice.artist-metadata.config :as config]
   [ice.artist-metadata.env :refer [defaults]]

    ;; Edges  
   [kit.edge.scheduling.quartz]
   [kit.edge.db.sql.conman]
   [kit.edge.db.sql.migratus]
   [kit.edge.db.postgres]
   [kit.edge.db.mysql]
   [kit.edge.utils.metrics]
   [kit.edge.server.undertow]
   [ice.artist-metadata.web.handler]
   [ice.artist-metadata.tasks.handler]

    ;; Routes
   [ice.artist-metadata.web.routes.api]

   [ice.artist-metadata.components.hato])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error {:what :uncaught-exception
                 :exception ex
                 :where (str "Uncaught exception on" (.getName thread))}))))

(defonce system (atom nil))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!))
  (shutdown-agents))

(defn start-app [& [params]]
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/prep)
       (ig/init)
       (reset! system))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& _]
  (start-app))
