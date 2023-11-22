-- Documentation for commands: https://www.hugsql.org/hugsql-in-detail/command
-- Documentation for responses: https://www.hugsql.org/hugsql-in-detail/result

-- :name save-track! :<!
-- :doc creates a new track record
INSERT INTO track
(id, name, artist_id, length_in_seconds)
VALUES (:id, :name, :artist-id, :length-in-seconds)
RETURNING *;

-- :name save-artist! :<!
-- :doc creates a new artist record
INSERT INTO artist
(name, year_first_active)
VALUES (:name, :year-first-active)
RETURNING *;

-- :name save-artist-alias! :<!
-- :doc creates a new artist alias record
INSERT INTO artist_alias
(alias, artist_id)
VALUES (:alias, :artist-id)
RETURNING *;

-- :name save-genre! :<!
-- :doc creates a new genre record
INSERT INTO genre
(name)
VALUES (:name)
RETURNING *;

-- :name save-track-genre! :<!
-- :doc creates a new artist record
INSERT INTO track_genre
(track_id, genre_id)
VALUES (:track-id, :genre-id)
RETURNING *;


-- :name update-artist! :! :*
-- :doc updates artist's name
UPDATE artist
SET name = :name, year_first_active = :year-first-active
WHERE id = :id
RETURNING *;

-- :name fetch-tracks-by-artist-id! :? :*
-- :doc get all tracks for a single artist
SELECT * FROM track
WHERE artist_id = :artist-id;

-- :name fetch-artist-of-day! :? :1
-- :doc get all tracks for a single artist
SELECT a.* 
FROM artist_of_day AS aod
JOIN artist AS a
ON (aod.date_featured = :date) AND (a.id = aod.artist_id);

-- :name set-artist-of-the-day-for-three-days! :<!
-- :doc if date has no artist-of-the-day, a (never/least-recently)-featured user
WITH next_artist AS
    (SELECT (CURRENT_DATE - 1) AS date_featured, a.id AS artist_id
    FROM artist AS a
    LEFT JOIN artist_of_day AS aod ON (aod.artist_id = a.id) and (aod.date_featured <= (CURRENT_DATE + 1))
    GROUP BY a.id
    ORDER BY MAX(aod.date_featured) NULLS FIRST
    LIMIT 1)
INSERT INTO artist_of_day 
SELECT * from next_artist
ON CONFLICT (date_featured) DO NOTHING;
WITH next_artist AS
    (SELECT (CURRENT_DATE) AS date_featured, a.id AS artist_id
    FROM artist AS a
    LEFT JOIN artist_of_day AS aod ON (aod.artist_id = a.id) and (aod.date_featured <= (CURRENT_DATE + 1))
    GROUP BY a.id
    ORDER BY MAX(aod.date_featured) NULLS FIRST
    LIMIT 1)
INSERT INTO artist_of_day 
SELECT * from next_artist
ON CONFLICT (date_featured) DO NOTHING;
WITH next_artist AS
    (SELECT (CURRENT_DATE + 1) AS date_featured, a.id AS artist_id
    FROM artist AS a
    LEFT JOIN artist_of_day AS aod ON (aod.artist_id = a.id) and (aod.date_featured <= (CURRENT_DATE + 1))
    GROUP BY a.id
    ORDER BY MAX(aod.date_featured) NULLS FIRST
    LIMIT 1)
INSERT INTO artist_of_day 
SELECT * from next_artist
ON CONFLICT (date_featured) DO NOTHING;
