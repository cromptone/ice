(ns ice.artist-metadata.web.controllers.artists
  (:require [clojure.string :as string]
            [clojure.tools.logging :as log]
            [next.jdbc :as jdbc]
            [ring.util.http-response :as http-response]))


(defn save-artist! [{:keys [query-fn db-conn] :as opts}
                    {{{name :name
                       year-first-active :year-first-active} :body} :parameters}]

  (try (-> (query-fn :save-artist!
                     {:name (string/trim name)
                      :year-first-active year-first-active})
           first
           http-response/ok)
       (catch Exception _e
         (http-response/internal-server-error _e))))

(defn update-artist! [{:keys [query-fn] :as opts}
                      {{{year-first-active :year-first-active
                         name :name} :body
                        {id :id} :path} :parameters}]

  (-> (query-fn :update-artist!
                {:year-first-active year-first-active
                 :name (string/trim name)
                 :id (parse-uuid id)})
      first
      http-response/ok))

(defn fetch-tracks-by-artist-id! [{:keys [query-fn] :as opts}
                                  {{{artist-id :artist-id} :path} :parameters}]
  (-> (query-fn :fetch-tracks-by-artist-id! {:artist-id (parse-uuid artist-id)})
      http-response/ok))