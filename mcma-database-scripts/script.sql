create table if not exists authority
(
    ID           smallint auto_increment
        primary key,
    CODE         varchar(20)  null,
    NAME         varchar(255) null,
    TYPE         smallint     null comment '1: Admin; 2: User',
    STATUS       smallint     null comment '1: Active; -1: Inactive',
    DATE_CREATED timestamp    null,
    DATE_UPDATED timestamp    null
);

create table if not exists cinema
(
    ID               bigint unsigned                   not null
        primary key,
    CITY_ID          bigint unsigned                   not null,
    NAME             varchar(50)                       null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists city
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(30)                       null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists coupon
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    DISCOUNT         decimal(3, 2)                     null,
    MIN_SPEND_REQ    int                               null,
    DISCOUNT_LIMIT   int                               null,
    DATE_AVAILABLE   timestamp default utc_timestamp() null,
    DATE_EXPIRED     timestamp default utc_timestamp() null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists drink
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    SIZE             varchar(5)                        null,
    VOLUME           smallint                          null,
    PRICE            int                               null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists food
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    SIZE             varchar(5)                        null,
    PRICE            int                               null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(50)                       null,
    LENGTH           int                               null,
    DATE_PUBLISH     timestamp default utc_timestamp() null,
    RATING_ID        tinyint                           null,
    TRAILER_LINK     text                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie_coupon
(
    MOVIE_ID  bigint unsigned not null,
    COUPON_ID bigint unsigned not null,
    primary key (MOVIE_ID, COUPON_ID)
);

create table if not exists movie_genre
(
    MOVIE_ID bigint unsigned not null,
    GENRE_ID bigint unsigned not null,
    primary key (MOVIE_ID, GENRE_ID)
);

create table if not exists movie_genre_detail
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie_performer
(
    MOVIE_ID     bigint unsigned not null,
    PERFORMER_ID bigint unsigned not null,
    primary key (MOVIE_ID, PERFORMER_ID)
);

create table if not exists movie_performer_detail
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(50)                       null,
    TYPE             tinyint                           null comment '1: director; 2: actor',
    SEX              tinyint                           null comment '0: male; 1: female',
    DOB              date                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie_rating_detail
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie_response
(
    ID               bigint unsigned                   not null
        primary key,
    USER_ID          bigint unsigned                   not null,
    MOVIE_ID         bigint unsigned                   not null,
    USER_RATING      tinyint                           null,
    USER_COMMENT     text                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists movie_schedule
(
    ID               bigint unsigned                   not null
        primary key,
    SCREEN_ID        bigint unsigned                   not null,
    MOVIE_ID         bigint unsigned                   not null,
    START_TIME       timestamp default utc_timestamp() null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists screen
(
    ID               bigint unsigned                   not null
        primary key,
    CINEMA_ID        bigint unsigned                   not null,
    NAME             varchar(20)                       null,
    TYPE_ID          tinyint                           null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists screen_type
(
    ID               bigint unsigned                   not null
        primary key,
    NAME             varchar(20)                       null,
    DESCRIPTION      text                              null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists seat
(
    ID               bigint unsigned                   not null
        primary key,
    SCREEN_ID        bigint unsigned                   not null,
    ROW              tinyint unsigned                  null,
    COL              tinyint unsigned                  null,
    TYPE             tinyint                           null,
    NAME             varchar(5)                        null,
    CREATED_BY       bigint unsigned                   null,
    LAST_MODIFIED_BY bigint unsigned                   null,
    DATE_CREATED     timestamp default utc_timestamp() null,
    DATE_UPDATED     timestamp default utc_timestamp() null
);

create table if not exists user
(
    ID               bigint unsigned auto_increment
        primary key,
    EMAIL            varchar(255)    null,
    PASSWORD         varchar(255)    null,
    USER_TYPE        smallint        null,
    STATUS           smallint        null,
    ACTIVATION_KEY   varchar(20)     null,
    RESET_KEY        varchar(20)     null,
    DELETE_KEY       varchar(20)     null,
    DATE_LAST_LOGIN  timestamp       null,
    DATE_RESET       timestamp       null,
    DATE_DELETE      timestamp       null,
    CREATED_BY       bigint unsigned null,
    LAST_MODIFIED_BY bigint unsigned null,
    DATE_CREATED     timestamp       null,
    DATE_UPDATED     timestamp       null
);

create table if not exists user_authority
(
    ID           int auto_increment
        primary key,
    USER_ID      bigint unsigned not null,
    AUTHORITY_ID smallint        null,
    STATUS       smallint        null comment '1: Active; -1: Inactive'
);

create index if not exists FK_user_authority_authority
    on user_authority (AUTHORITY_ID);

create index if not exists FK_user_authority_user
    on user_authority (USER_ID);

create table if not exists user_coupon
(
    USER_ID   bigint unsigned not null,
    COUPON_ID bigint unsigned not null,
    primary key (USER_ID, COUPON_ID)
);


