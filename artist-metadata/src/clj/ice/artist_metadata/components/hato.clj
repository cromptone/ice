(ns ice.artist-metadata.components.hato
  (:require
   [integrant.core :as ig]
   [hato.client :as hc]))

(defmethod ig/init-key :http/hato [_ opts]
  (hc/build-http-client opts))

(defmethod ig/halt-key! :http/hato [_ _http-client]
  nil)
