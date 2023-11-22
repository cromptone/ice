(ns ice.artist-metadata.web.controllers.genres
  (:require [clojure.string :as string]
            [ring.util.http-response :as http-response]))


(defn save-genre! [{:keys [query-fn] :as opts}
                   {{{name :name} :body} :parameters}]
  (try (-> (query-fn :save-genre!
                     {:name (string/trim name)})
           first
           http-response/ok)
       (catch Exception e
         (http-response/internal-server-error e))))
