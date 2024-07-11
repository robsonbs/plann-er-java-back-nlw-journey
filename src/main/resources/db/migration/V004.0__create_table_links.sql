CREATE TABLE links
(
    id      uuid DEFAULT random_uuid() PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    url     VARCHAR(255) NOT NULL,
    trip_id uuid         NOT NULL REFERENCES trips (id) ON DELETE CASCADE
);