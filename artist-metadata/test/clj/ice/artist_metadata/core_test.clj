(ns ice.artist-metadata.core-test
  (:require
   [ice.artist-metadata.test-utils :as utils]
   [clojure.test :refer :all]
   [ice.artist-metadata.web.controllers.health :as health]
   [ice.artist-metadata.web.controllers.artists :as artists]
   [ice.artist-metadata.web.controllers.tracks :as tracks]
   [ice.artist-metadata.web.controllers.artist-of-day :as aod]))

(use-fixtures :once utils/test-fixtures)


(deftest test-health-check
  (testing "Make healthcheck"
    (let [{status :status
           {:keys [up-since app] :as body} :body} (health/healthcheck! (utils/test-ctx))]
      (is (= 200 status))
      (is (string? up-since))
      (is (= (:status app) "up")))))