INSERT INTO motorcycles (id, model, manufacturer, inventory, fee, type, is_deleted)
VALUES (91, 'WR250F', 'Yamaha', 5, 100.00, 'ENDURO', false);

INSERT INTO users (id, email, first_name, last_name, password, telegram_id)
VALUES (91, 'user1@mail.com', 'firstName', 'lastName', '11111111', 11111111);

INSERT INTO rentals (id, return_date, rental_date, actual_return_date, motorcycle_id, user_id)
VALUES (91, '2023-01-02 00:30:31', '2023-01-01 00:30:31', '2023-01-02 00:20:31', 91, 91);

INSERT INTO rentals (id, return_date, rental_date, actual_return_date, motorcycle_id, user_id)
VALUES (92, '2023-01-02 00:30:31', '2023-01-01 00:30:31', NULL, 91, 91);
