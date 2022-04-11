DELETE
FROM MENU_WITH_DISHES;
DELETE
FROM vote;
DELETE
FROM dish;
DELETE
FROM menu;
DELETE
FROM restaurant;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@gmail.com', 'user'),
       ('User2', 'user2@gmail.com', 'user2'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100002),
       ('USER', 100002);

INSERT INTO restaurant (name)
VALUES ('USA'),
       ('Italia'),
       ('Ukraine'),
       ('France');

INSERT INTO menu (menu_date, restaurant_id)
VALUES ('2022-03-01', 100004),
       ('2022-03-02', 100004),
       (CURRENT_DATE, 100004),
       ('2022-03-01', 100005),
       ('2022-03-02', 100005),
       (CURRENT_DATE, 100005),
       ('2022-03-01', 100006),
       ('2022-03-02', 100006),
       (CURRENT_DATE, 100006),
       ('2022-03-01', 100007),
       ('2022-03-02', 100007),
       (CURRENT_DATE, 100007);

INSERT INTO dish (name, price, restaurant_id)
VALUES ('AmFood1', '10.99', 100004),
       ('AmFood2', '20.99', 100004),
       ('ItFood1', '10.50', 100005),
       ('ItFood2', '20.50', 100005),
       ('ItFood3', '30.50', 100005),
       ('UkFood1', '10', 100006),
       ('UkFood2', '20', 100006),
       ('UkFood3', '30', 100006),
       ('UkFood4', '40', 100006),
       ('FrFood1', '100', 100007),
       ('FrFood2', '200', 100007),
       ('FrFood3', '300', 100007),
       ('FrFood4', '400', 100007),
       ('FrFood5', '500', 100007);

INSERT INTO MENU_WITH_DISHES (menu_id, dish_id)
VALUES (100008, 100020),
       (100009, 100021),
       (100010, 100020),
       (100010, 100021),
       (100011, 100022),
       (100011, 100023),
       (100012, 100022),
       (100012, 100024),
       (100013, 100022),
       (100013, 100023),
       (100013, 100024),
       (100014, 100025),
       (100014, 100026),
       (100014, 100027),
       (100015, 100026),
       (100015, 100027),
       (100015, 100028),
       (100016, 100025),
       (100014, 100026),
       (100014, 100028),
       (100017, 100029),
       (100017, 100030),
       (100017, 100031),
       (100018, 100029),
       (100018, 100030),
       (100018, 100032),
       (100019, 100029),
       (100019, 100030),
       (100019, 100031),
       (100019, 100033);

INSERT INTO vote (vote_date, user_id, restaurant_id)
VALUES ('2022-03-01', 100000, 100006),
       ('2022-03-01', 100001, 100006),
       (CURRENT_DATE, 100000, 100005),
       (CURRENT_DATE, 100001, 100004),
       (CURRENT_DATE, 100002, 100005);