(ns ice.artist-metadata.test-utils
  (:require
   [ice.artist-metadata.core :as core]
   [ice.artist-metadata.test-utils :as utils]
   [clojure.test :refer [join-fixtures]]
   [integrant.repl.state :as state]
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]))

(defn system-state
  []
  (or @core/system state/system))

(defn test-ctx
  []
  (let [{:keys [:db.sql/query-fn :http/hato :db.sql/connection]} (system-state)]
    {:query-fn    query-fn
     :http-client hato
     :db-conn connection}))

(defn clear-db []
  (jdbc/execute! (:db.sql/connection (system-state))
                 ["do
$$
    declare
        row record;
    begin
        for row in select * from pg_tables where schemaname = 'public'
            loop
                execute 'drop table public.' || quote_ident(row.tablename) || ' cascade';
            end loop;
    end;
$$;"]))

(defn run-migrations []
  (migratus.core/migrate (:db.sql/migrations (system-state))))


(defn db-fixture [f]
  (run-migrations)
  (f)
  (clear-db))

(defn system-fixture [f]
  (when (nil? (system-state))
    (core/start-app {:opts {:profile :test}}))
  (f))

(def test-fixtures (join-fixtures [system-fixture
                                   db-fixture]))