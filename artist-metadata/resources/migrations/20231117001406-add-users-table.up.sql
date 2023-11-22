CREATE TABLE artist
(id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 name VARCHAR(200) NOT NULL,
 year_first_active SMALLINT
);
--;;
CREATE TABLE artist_alias
(id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 artist_id UUID REFERENCES artist(id) NOT NULL,
 alias VARCHAR(200) NOT NULL
);
--;;
CREATE TABLE track
(id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 name VARCHAR(200) NOT NULL,
 artist_id UUID REFERENCES artist(id) NOT NULL,
 length_in_seconds SMALLINT
);
--;;
CREATE INDEX fk_track_artist_id ON track(artist_id)
--;;
CREATE TABLE genre
(id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 name VARCHAR(200) UNIQUE NOT NULL
);
--;;
CREATE TABLE track_genre
(track_id UUID REFERENCES track(id) NOT NULL,
 genre_id UUID REFERENCES genre(id) NOT NULL,
 PRIMARY KEY (track_id, genre_id)
);
--;;
CREATE TABLE artist_of_day
(date_featured DATE PRIMARY KEY,
 artist_id UUID REFERENCES artist(id) NOT NULL
);
--;;
INSERT INTO artist (id, name, year_first_active)
VALUES 
('aaaaa55b-2a5c-47c0-8041-6605172d7a64','The Beach Boys', 1960),
('bbbba55b-2a5c-47c0-8041-6605172d7a64','Prince', 1980),
('cccca55b-2a5c-47c0-8041-6605172d7a64','Otis Redding', 1960),
('dddda55b-2a5c-47c0-8041-6605172d7a64','Nina Simone', 1960);
--;;
INSERT INTO artist_alias (alias, artist_id)
VALUES ('The Beach Boyz', 'aaaaa55b-2a5c-47c0-8041-6605172d7a64');