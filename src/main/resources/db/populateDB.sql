DELETE FROM vote;
DELETE FROM dish;
DELETE FROM menu;
DELETE FROM restaurant;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name , email, password)
VALUES ('User', 'user@gmail.com', 'user'),
       ('User2', 'user2@gmail.com', 'user2'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100002);

INSERT INTO restaurant (name)
VALUES ('USA'),
       ('Italia'),
       ('Ukraine'),
       ('France');

INSERT INTO menu (menu_date, restaurant_id)
VALUES  (2022-03-01, 100004),
        (2022-03-02, 100004),
        (2022-03-03, 100004),
        (2022-03-01, 100005),
        (2022-03-01, 100005),
        (2022-03-01, 100005),
        (2022-03-01, 100006),
        (2022-03-01, 100006),
        (2022-03-01, 100006),
        (2022-03-01, 100007),
        (2022-03-01, 100007),
        (2022-03-01, 100007);

INSERT INTO dish (name, price,restaurant_id)
VALUES ('AmFood1','10.99', 100004),
       ('AmFood2','20.99', 100004),
       ('ItFood1','10.50', 100005),
       ('ItFood2','20.50', 100005),
       ('ItFood3','30.50', 100005),
       ('UkFood1','10', 100006),
       ('UkFood2','20', 100006),
       ('UkFood3','30', 100006),
       ('UkFood4','40', 100006),
       ('FrFood1','100.99', 100007),
       ('FrFood2','200.99', 100007),
       ('FrFood3','300.99', 100007),
       ('FrFood4','400.99', 100007),
       ('FrFood5','500.99', 100007);

INSERT INTO vote (vote_date, user_id, restaurant_id)
VALUES (2022-03-01, 100000, 100006),
       (2022-03-01, 100001, 100006),
       (2022-03-02, 100000, 100005),
       (2022-03-02, 100003, 100004),
       (2022-03-02, 100000, 100006),
       (2022-03-02, 100001, 100005);