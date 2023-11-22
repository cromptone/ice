(ns ice.artist-metadata.artist-alias-test
  (:require
   [ice.artist-metadata.test-utils :as utils]
   [clojure.test :refer :all]
   [ice.artist-metadata.web.controllers.artist-alias :as artist-alias]
   [ice.artist-metadata.web.controllers.artists :as artists]
   [ice.artist-metadata.web.controllers.tracks :as tracks]
   [ice.artist-metadata.web.controllers.artist-of-day :as aod]))

(use-fixtures :once utils/test-fixtures)

(deftest test-creating-artist-alias
  (testing "Create an artist and then fetch artist with good data"
    (let [artist-response (artists/save-artist! (utils/test-ctx) {:parameters {:body {:name "Artist Choir"
                                                                                      :year-first-active 1925}}})
          artist-id (get-in artist-response [:body :id])
          response (artist-alias/save-artist-alias! (utils/test-ctx) {:parameters {:path {:artist-id (str artist-id)}
                                                                                   :body {:alias "Alias Name"}}})
          {status :status
           {:keys [id alias]} :body} response]
      (is (= 200 status))
      (is (uuid? id))
      (is (= "Alias Name" alias)))))
