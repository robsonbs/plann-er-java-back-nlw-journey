CREATE TABLE activities
(
    id        uuid                  DEFAULT random_uuid() PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    occurs_at TIMESTAMP    NOT NULL,
    is_done   BOOLEAN      NOT NULL DEFAULT FALSE,
    trip_id   uuid         NOT NULL REFERENCES trips (id) ON DELETE CASCADE
);