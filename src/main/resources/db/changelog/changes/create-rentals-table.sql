CREATE TABLE IF NOT EXISTS rentals
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    return_date        DATETIME                          NOT NULL,
    rental_date        DATETIME                          NOT NULL,
    actual_return_date DATETIME,
    motorcycle_id      BIGINT                            NOT NULL,
    user_id            BIGINT                            NOT NULL,
    FOREIGN KEY (motorcycle_id) REFERENCES motorcycles (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
