CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    email       VARCHAR(255)                      NOT NULL UNIQUE,
    first_name  VARCHAR(255)                      NOT NULL,
    last_name   VARCHAR(255)                      NOT NULL,
    password    VARCHAR(255)                      NOT NULL,
    role        varchar(255) DEFAULT NULL,
    telegram_id BIGINT UNIQUE
);
