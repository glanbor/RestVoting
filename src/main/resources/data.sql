INSERT INTO users (name, email, password)
VALUES ('User', 'user@gmail.com', '{noop}user'),
       ('User2', 'user2@gmail.com', '{noop}user2'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('USER',2),
       ('ADMIN', 3),
       ('USER', 3);

INSERT INTO restaurant (name)
VALUES ('USA'),
       ('Italia'),
       ('Ukraine'),
       ('France');

INSERT INTO menu (menu_date, restaurant_id)
VALUES ('2022-03-01', 1),
       ('2022-03-02',1),
       (CURRENT_DATE, 1),
       ('2022-03-01', 2),
       ('2022-03-02', 2),
       (CURRENT_DATE,2),
       ('2022-03-01', 3),
       ('2022-03-02', 3),
       (CURRENT_DATE, 3),
       ('2022-03-01', 4),
       ('2022-03-02',4),
       (CURRENT_DATE, 4);

INSERT INTO dish (name, price, restaurant_id)
VALUES ('AmFood1', '10.99', 1),
       ('AmFood2', '20.99', 1),
       ('ItFood1', '10.50', 2),
       ('ItFood2', '20.50', 2),
       ('ItFood3', '30.50', 2),
       ('UkFood1', '10', 3),
       ('UkFood2', '20', 3),
       ('UkFood3', '30', 3),
       ('UkFood4', '40', 3),
       ('FrFood1', '100', 4),
       ('FrFood2', '200', 4),
       ('FrFood3', '300', 4),
       ('FrFood4', '400', 4),
       ('FrFood5', '500', 4);

INSERT INTO MENU_WITH_DISHES (menu_id, dish_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 3),
       (4, 4),
       (5, 3),
       (5, 5),
       (6, 3),
       (6, 4),
       (6, 5),
       (7, 6),
       (7, 7),
       (8, 7),
       (8, 8),
       (8, 9),
       (9, 6),
       (9, 7),
       (9, 9),
       (10, 10),
       (10, 11),
       (10, 12),
       (11, 10),
       (11, 11),
       (11, 13),
       (12, 10),
       (12, 11),
       (12, 12),
       (12, 14);

INSERT INTO vote (vote_date, user_id, restaurant_id)
VALUES ('2022-03-01', 1,3),
       ('2022-03-01', 2, 3),
       (CURRENT_DATE, 1, 2),
       (CURRENT_DATE, 3, 2);