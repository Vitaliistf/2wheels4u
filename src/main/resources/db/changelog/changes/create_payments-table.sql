CREATE TABLE IF NOT EXISTS payments
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    url            VARCHAR(500),
    session_id     VARCHAR(255),
    payment_amount DECIMAL(10, 2)                    NOT NULL,
    rental_id      BIGINT                            NOT NULL,
    status         VARCHAR(255)                      NOT NULL,
    type           VARCHAR(255)                      NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES rentals (id)
);
