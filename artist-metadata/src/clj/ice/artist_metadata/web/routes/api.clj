(ns ice.artist-metadata.web.routes.api
  (:require [ice.artist-metadata.web.controllers.artist-alias :as artist-alias]
            [ice.artist-metadata.web.controllers.artist-of-day :as artist-of-day]
            [ice.artist-metadata.web.controllers.artists :as artists]
            [ice.artist-metadata.web.controllers.genres :as genres]
            [ice.artist-metadata.web.controllers.health :as health]
            [ice.artist-metadata.web.controllers.tracks :as tracks]
            [ice.artist-metadata.web.middleware.exception :as exception]
            [ice.artist-metadata.web.middleware.formats :as formats]
            [ice.artist-metadata.web.routes.utils :refer [string-valid? yyyy-MM-dd-str?]]
            [integrant.core :as ig]
            [reitit.coercion.malli :as malli]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.swagger :as swagger]
            [ring.logger :as ring-logger]))

(def Artist
  [:maybe
   [:map
    [:id {:title "Artist UUID"
          :json-schema/default "aaaaa55b-2a5c-47c0-8041-6605172d7a64"}
     uuid?]
    [:name {:json-schema/default "Blondie"
            :min 1} string?]
    [:year_first_active {:json-schema/default 1974}
     int?]]])

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id ::api}
   :middleware [;; query-params & form-params
                parameters/parameters-middleware
                  ;; content-negotiation
                muuntaja/format-negotiate-middleware
                  ;; encoding response body
                muuntaja/format-response-middleware
                  ;; exception handling
                coercion/coerce-exceptions-middleware
                  ;; decoding request body
                muuntaja/format-request-middleware
                  ;; log incoming requests
                ring-logger/wrap-with-logger
                  ;; coercing response bodys
                coercion/coerce-response-middleware
                  ;; coercing request parameters
                coercion/coerce-request-middleware
                  ;; exception handling
                exception/wrap-exception]})

;; Routes
(defn api-routes [opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "API Documentation"}}
           :handler (swagger/create-swagger-handler)}}]

   ["/artist-of-day"
    {:get {:summary    "Get artist of the day for date"
           :parameters {:query [:map
                                [:date {:title "Date"
                                        :description "The date, formatted yyyy-MM-dd (with dashes)."
                                        :json-schema/default "2023-12-31"}
                                 [:fn yyyy-MM-dd-str?]]]}
           :responses  {200 {:body Artist}}
           :handler    (partial artist-of-day/fetch-artist-of-day! opts)}}]
   ["/artists"
    ["" {:post {:summary    "Create an Artist"
                :parameters {:body [:map
                                    [:name [:fn string-valid?]]
                                    [:year-first-active {:optional true} pos-int?]]}
                :handler    (partial artists/save-artist! opts)}}]
    ["/:id" {:put {:summary    "Update an Artist"
                   :parameters {:path {:id string?}
                                :body [:map
                                       [:name [:fn string-valid?]]
                                       [:year-first-active {:optional true} pos-int?]]}

                   :handler    (partial artists/update-artist! opts)}}]

    ["/:artist-id/artist-alias" {:post {:summary    "Add an Artist Alias"
                                        :parameters {:path {:artist-id string?}
                                                     :body [:map
                                                            [:alias [:fn string-valid?]]]}
                                        :handler    (partial artist-alias/save-artist-alias! opts)}}]
    ["/:artist-id/tracks" {:get {:summary    "Get all artist's tracks"
                                 :parameters {:path {:artist-id string?}}
                                 :handler    (partial artists/fetch-tracks-by-artist-id! opts)}}]]

   ["/tracks"
    ["" {:post {:summary    "Create a Track"
                :parameters {:body [:map
                                    [:name string?]
                                    [:artist_id string?]
                                    [:genre-ids {:title "Genre ids"
                                                 :optional true
                                                 :description "A list of UUIDs of Genres represented by the track."
                                                 :json-schema/default ["7ec2507c-1c5f-4834-b086-3af5d7fd8fc1"]}
                                     [:fn vector?]]
                                    [:length_in_seconds int?]]}
                :handler    (partial tracks/save-track! opts)}}]]

   ["/genres"
    ["" {:post {:summary    "Create a Genre"
                :parameters {:body [:map
                                    [:name [:fn string-valid?]]]}
                :handler    (partial genres/save-genre! opts)}}]]

   ["/health"
    {:get health/healthcheck!}]])

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  [base-path route-data (api-routes opts)])
