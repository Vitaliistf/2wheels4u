CREATE TABLE IF NOT EXISTS motorcycles
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    model        VARCHAR(255)                      NOT NULL,
    manufacturer VARCHAR(255)                      NOT NULL,
    inventory    INT                               NOT NULL,
    fee          DECIMAL(10, 2)                    NOT NULL,
    type         VARCHAR(255)                      NOT NULL,
    is_deleted   BOOLEAN                           NOT NULL DEFAULT FALSE
);
