CREATE TABLE participants
(
    id           uuid DEFAULT random_uuid() PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    is_confirmed BOOLEAN      NOT NULL DEFAULT FALSE,
    trip_id      uuid         NOT NULL REFERENCES trips (id) ON DELETE CASCADE
);