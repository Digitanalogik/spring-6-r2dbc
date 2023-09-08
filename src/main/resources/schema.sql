CREATE TABLE IF NOT EXISTS beer
(
    id                  INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    beer_name           VARCHAR(255),
    beer_style          VARCHAR(255),
    upc                 VARCHAR(25),
    quantity_on_hand    INTEGER,
    price               NUMERIC(7,2),
    created_date        TIMESTAMP,
    updated_date        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer
(
    id              INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255),
    created_date    TIMESTAMP,
    updated_date    TIMESTAMP
);
