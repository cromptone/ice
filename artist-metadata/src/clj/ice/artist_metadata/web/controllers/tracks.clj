(ns ice.artist-metadata.web.controllers.tracks
  (:require [clojure.string :as string]
            [next.jdbc :as jdbc]
            [ring.util.http-response :as http-response]))

(defn save-track! [{:keys [query-fn db-conn] :as opts}
                   {{{name :name
                      genre-ids :genre-ids
                      artist-id :artist_id
                      length-in-seconds :length_in_seconds} :body} :parameters}]
  (let [track-id (java.util.UUID/randomUUID)]
    (-> (jdbc/with-transaction [tx db-conn]
          (let [response (query-fn tx :save-track! {:id track-id
                                                    :name name
                                                    :artist-id (parse-uuid artist-id)
                                                    :length-in-seconds length-in-seconds})]

            (doseq [genre-id genre-ids]
              (query-fn tx :save-track-genre! {:track-id track-id
                                               :genre-id (parse-uuid genre-id)}))
            response))

        first
        http-response/ok)))


