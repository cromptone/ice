(ns ice.artist-metadata.artist-test
  (:require
   [ice.artist-metadata.test-utils :as utils]
   [clojure.test :refer :all]
   [ice.artist-metadata.web.controllers.health :as health]
   [ice.artist-metadata.web.controllers.artists :as artists]
   [ice.artist-metadata.web.controllers.tracks :as tracks]
   [ice.artist-metadata.web.controllers.artist-of-day :as aod]))

(use-fixtures :once utils/test-fixtures)

(deftest test-creating-and-updating-artist
  (testing "Create an artist and then fetch artist with good data"
    (let [response (artists/save-artist! (utils/test-ctx) {:parameters {:body {:name "Artist Choir"
                                                                               :year-first-active 1925}}})
          {status :status
           {:keys [year_first_active name id]} :body} response]
      (is (= 200 status))
      (is (uuid? id))
      (is (= 1925 year_first_active))
      (is (= "Artist Choir" name))

      (let [fetch-response (artists/update-artist!
                            (utils/test-ctx)
                            {:parameters {:path {:id (str id)}
                                          :body {:name "New name"
                                                 :year-first-active 1990}}})
            fetched-year-first-active (get-in fetch-response [:body :year_first_active])
            fetched-name (get-in fetch-response [:body :name])]
        (is (= 200 status))
        (is (= 1990 fetched-year-first-active))
        (is (= "New name" fetched-name))))))
