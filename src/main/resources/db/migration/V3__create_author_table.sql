CREATE TABLE author (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL
);