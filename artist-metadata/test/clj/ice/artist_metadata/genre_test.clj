(ns ice.artist-metadata.genre-test
  (:require
   [ice.artist-metadata.test-utils :as utils]
   [clojure.test :refer :all]
   [ice.artist-metadata.web.controllers.genres :as genres]
   [ice.artist-metadata.web.controllers.artists :as artists]
   [ice.artist-metadata.web.controllers.tracks :as tracks]
   [ice.artist-metadata.web.controllers.artist-of-day :as aod]))

(use-fixtures :once utils/test-fixtures)


(deftest test-creating-genre
  (testing "Create a genre"
    (let [response (genres/save-genre! (utils/test-ctx) {:parameters {:body {:name "Pop Music"}}})
          {status :status
           {:keys [id name]} :body} response]
      (is (= 200 status))
      (is (uuid? id))
      (is (= "Pop Music" name)))))