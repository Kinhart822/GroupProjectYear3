set foreign_key_checks = 0;
truncate table mcma.booking;
truncate table mcma.cinema;
truncate table mcma.city;
truncate table mcma.coupon;
truncate table mcma.drink;
truncate table mcma.food;
truncate table mcma.genre;
truncate table mcma.map_booking_coupon;
truncate table mcma.map_booking_drink;
truncate table mcma.map_booking_food;
truncate table mcma.map_booking_ticket;
truncate table mcma.map_movie_coupon;
truncate table mcma.map_movie_genre;
truncate table mcma.map_movie_performer;
truncate table mcma.map_user_coupon;
truncate table mcma.movie;
truncate table mcma.movie_response;
truncate table mcma.notification;
truncate table mcma.performer;
truncate table mcma.rating;
truncate table mcma.schedule;
truncate table mcma.screen;
truncate table mcma.screen_type;
truncate table mcma.seat;
truncate table mcma.ticket;
truncate table mcma.token;
truncate table mcma.user;
set foreign_key_checks = 1;
insert into mcma.city (name, status, created_by, created_date, last_modified_by, last_modified_date)
values ('Hanoi', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Ho Chi Minh City', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Da Nang', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Hai Phong', 1, 1, utc_timestamp(), 1, utc_timestamp());
insert into mcma.cinema (city_id, address, name, status, created_by, created_date, last_modified_by, last_modified_date)
values (1, 'Cau Giay', 'Hanoi Cinema 1', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (1, 'Cau Giay', 'Hanoi Cinema 2', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (1, 'Cau Giay', 'Hanoi Cinema 3', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (1, 'Cau Giay', 'Hanoi Cinema 4', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (2, 'Cau Giay', 'HCMC Cinema 1', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (2, 'Cau Giay', 'HCMC Cinema 2', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (2, 'Cau Giay', 'HCMC Cinema 3', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (2, 'Cau Giay', 'HCMC Cinema 4', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (3, 'Cau Giay', 'Da Nang Cinema 1', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (3, 'Cau Giay', 'Da Nang Cinema 2', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (4, 'Cau Giay', 'Hai Phong Cinema 1', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (4, 'Cau Giay', 'Hai Phong Cinema 2', 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (4, 'Cau Giay', 'Hai Phong Cinema 3', 1, 1, utc_timestamp(), 1, utc_timestamp());
-- not updated
# insert into mcma.coupon (name, description, discount, min_spend_req, discount_limit, available_date, expired_date,
#                          status, created_by, last_modified_by)
# values
#     -- Small discount, no minimum spend
#     ('SAVE5', '5% off on any order', 0.05, NULL, NULL, '2024-01-01 00:00:00', '2024-06-30 23:59:59', 1, 1, 1),
#     ('SAVE10', '10% off on orders above 100,000 VND', 0.10, 100000, 50000, '2024-01-01 00:00:00', '2024-12-31 23:59:59',
#      1, 1, 1),
#     ('SAVE20', '20% off up to 100,000 VND for orders above 200,000 VND', 0.20, 200000, 100000, '2024-02-01 00:00:00',
#      '2024-07-31 23:59:59', 1, 1, 1),
#     ('FLASH50', '50% off up to 200,000 VND for a limited time', 0.50, 500000, 200000, '2024-03-01 00:00:00',
#      '2024-03-15 23:59:59', 1, 1, 1),
#     ('SEASON20', '20% off on all products during the season sale', 0.20, NULL, NULL, '2024-06-01 00:00:00',
#      '2024-06-30 23:59:59', 1, 1, 1),
#     ('BIGSPEND50', '50,000 VND discount for orders above 1,000,000 VND', NULL, 1000000, 50000, '2024-01-01 00:00:00',
#      '2024-12-31 23:59:59', 1, 1, 1),
#     ('WELCOME', '30% off for new users up to 150,000 VND', 0.30, 50000, 150000, '2024-01-01 00:00:00',
#      '2024-12-31 23:59:59', 1, 1, 1);
# insert into mcma.drink (name, description, image_url, size, volume, price, status, created_by, last_modified_by)
# values ('Coke', 'Classic Coca-Cola', 'http://example.com/images/coke.jpg', 'XL', 16, 50000, 1, 1, 1),
#        ('Coke', 'Classic Coca-Cola', 'http://example.com/images/coke.jpg', 'L', 15, 48000, 1, 1, 1),
#        ('Coke', 'Classic Coca-Cola', 'http://example.com/images/coke.jpg', 'M', 14, 46000, 1, 1, 1),
#        ('Pepsi', 'Pepsi Cola', 'http://example.com/images/pepsi.jpg', 'XL', 16, 50000, 1, 1, 1),
#        ('Pepsi', 'Pepsi Cola', 'http://example.com/images/pepsi.jpg', 'L', 15, 48000, 1, 1, 1),
#        ('Pepsi', 'Pepsi Cola', 'http://example.com/images/pepsi.jpg', 'M', 51400, 46000, 1, 1, 1),
#        ('Orange Juice', 'Freshly squeezed orange juice', 'http://example.com/images/orange-juice.jpg', 'L', 15, 35000,
#         1, 1, 1);
# insert into mcma.food (name, description, image_url, size, price, status, created_by, last_modified_by)
# values ('Buttery Popcorn', 'Buttery popcorn', 'http://example.com/images/popcorn.jpg', 'L', 50000, 1, 1, 1),
#        ('Buttery Popcorn', 'Buttery popcorn', 'http://example.com/images/popcorn.jpg', 'M', 40000, 1, 1, 1),
#        ('Caramel Popcorn', 'Caramel popcorn', 'http://example.com/images/popcorn.jpg', 'L', 70000, 1, 1, 1),
#        ('Caramel Popcorn', 'Caramel popcorn', 'http://example.com/images/popcorn.jpg', 'M', 65000, 1, 1, 1),
#        ('Nachos', 'Crispy nachos with cheese dip', 'http://example.com/images/nachos.jpg', null, 40000, 1, 1, 1),
#        ('Hotdog', 'Juicy hotdog in a bun', 'http://example.com/images/hotdog.jpg', null, 35000, 1, 1, 1);
--
insert into mcma.screen_type (name, description, price, status, created_by, created_date, last_modified_by,
                              last_modified_date)
values ('imax', 'wow', 10, 1, 1, utc_timestamp(), 1, utc_timestamp());
insert into mcma.screen (cinema_id, name, type_id, status, mutable, created_by, created_date, last_modified_by,
                         last_modified_date)
values (1, 'phong 1', 1, 1, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       (1, 'phong 2', 1, 1, 1, 1, utc_timestamp(), 1, utc_timestamp());
insert into mcma.user (first_name, last_name, sex, dob, email, phone, password, address, user_type, status, created_by,
                       created_date, last_modified_by, last_modified_date)
values ('At', 'Nguyen', 1, '2004-04-11 00:00:00', 'at0@gmail.com', '0976289114',
        '$2a$10$do2lQSohzuLfiSljWORi0Oc3OVG37mtPWxR/cAmJSsOLebcF4A5Oi', 'Hanoi, Viet Nam', 0, 1, 0, utc_timestamp(), 0,
        utc_timestamp());
insert into mcma.rating (name, description, status, created_by, last_modified_by, created_date, last_modified_date)
values ('PG-13', 'Parental Guidance-13', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('R', 'Restricted', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('G', 'General Audience', 1, 1, 1, utc_timestamp(), utc_timestamp());

insert into mcma.movie (name, length, publish_date, rating_id, status, created_by, created_date, last_modified_by,
                        last_modified_date)
values ('Inception', 148, '2010-07-16 00:00:00', 1, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Interstellar', 169, '2014-11-07 00:00:00', 2, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('The Dark Knight', 152, '2008-07-18 00:00:00', 3, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Tenet', 150, '2020-08-26 00:00:00', 1, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('Dunkirk', 106, '2017-07-21 00:00:00', 2, 1, 1, utc_timestamp(), 1, utc_timestamp()),
       ('not released', 10, '2025-01-31 00:00:00', 3, 1, 1, utc_timestamp(), 1, utc_timestamp());
insert into mcma.genre (name, description, status, created_by, last_modified_by, created_date, last_modified_date)
values ('Action', 'Action-packed movie', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Comedy', 'Humorous movie', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Drama', 'Emotional storytelling', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Sci-fi', 'Science Fiction', 1, 1, 1, utc_timestamp(), utc_timestamp());
insert into mcma.map_movie_genre (movie_id, genre_id)
values (1,1),
       (1,2),
       (1,3),
       (2,2),
       (2,3),
       (2,4),
       (3,3),
       (3,4),
       (3,1),
       (4,4),
       (4,1),
       (4,2),
       (5,1),
       (5,2),
       (5,3),
       (6,2),
       (6,3),
       (6,4);

insert into mcma.performer (name, type_id, sex, dob, status, created_by, last_modified_by, created_date,
                            last_modified_date)
values ('John Doe', 1, 0, '1980-05-15 00:00:00', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Jane Smith', 0, 1, '1990-10-20 00:00:00', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Alice Johnson', 0, 1, '1985-03-25 00:00:00', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Bob Brown', 1, 0, '1975-12-05 00:00:00', 1, 1, 1, utc_timestamp(), utc_timestamp()),
       ('Charlie Davis', 1, 1, '1982-07-30 00:00:00', 1, 1, 1, utc_timestamp(), utc_timestamp());
insert into mcma.map_movie_performer (movie_id, performer_id)
values (1,1),
       (1,2),
       (1,3),
       (2,4),
       (2,5),
       (2,1),
       (3,2),
       (3,3),
       (3,4),
       (4,5),
       (4,1),
       (4,2),
       (5,3),
       (5,4),
       (5,5),
       (6,1),
       (6,2),
       (6,3);
