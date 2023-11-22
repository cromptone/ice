(ns ice.artist-metadata.web.controllers.artist-alias
  (:require
   [clojure.tools.logging :as log]
   [ring.util.http-response :as http-response]))

(defn save-artist-alias! [{:keys [query-fn] :as opts}
                          {{{alias :alias} :body
                            {artist-id :artist-id} :path} :parameters}]
  (-> (query-fn :save-artist-alias! {:artist-id (parse-uuid artist-id)
                                     :alias alias})
      first
      http-response/ok))