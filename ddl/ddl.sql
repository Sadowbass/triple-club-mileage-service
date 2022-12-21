# 데이터베이스 생성부터 하실경우엔 DB명이 triple_mileage_db 설정되어 있습니다.
create database triple_mileage_db;
# /src/test/resources/application.yml에는 triple_test_db로 설정되어 있습니다.
create database triple_test_db;

# Table 및 기본 Index DDL
create table users
(
    seq_id           int unsigned primary key auto_increment,
    user_id          binary(16) unique not null,
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;

create table place
(
    seq_id           int unsigned primary key auto_increment,
    place_id         binary(16) unique not null,
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;

create table review
(
    seq_id           int unsigned primary key auto_increment,
    review_id        binary(16) unique not null,
    content          mediumtext,
    user_seq_id      int unsigned      not null,
    place_seq_id     int unsigned      not null,
    is_first         bit               not null,
    is_delete        bit               not null,
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;
alter table review
    add index idx_review_place_user (place_seq_id, user_seq_id);
alter table review
    add index idx_review_user_seq_id_review_id (user_seq_id, review_id);


create table review_photos
(
    seq_id           int unsigned primary key auto_increment,
    review_seq_id    int unsigned not null,
    photo_id         binary(16)   not null,
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;
alter table review_photos
    add index idx_photos_review_seq_id (review_seq_id);

create table mileage
(
    seq_id           int unsigned primary key auto_increment,
    user_seq_id      int unsigned        not null,
    review_seq_id    int unsigned unique not null,
    review_mileage   tinyint             not null,
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;
alter table mileage
    add index idx_mileage_user_seq_id (user_seq_id);

create table mileage_detail
(
    seq_id           int unsigned primary key auto_increment,
    user_seq_id      int unsigned not null,
    review_seq_id    int unsigned not null,
    mileage_seq_id   int unsigned not null,
    event_action     varchar(10)  not null,
    changed_mileage  tinyint      not null,
    reason           varchar(20),
    created_at       datetime,
    last_modified_at datetime
) engine = InnoDB;
alter table mileage_detail
    add index idx_mileage_detail_mileage_seq_id (mileage_seq_id);

# 기본 user와 place 입력 쿼리
insert into users(user_id, created_at, last_modified_at)
values (unhex('3ede0ef292b74817a5f30c575361f745'), sysdate(), sysdate());

insert into place(place_id, created_at, last_modified_at)
values (unhex('2e4baf1c5acb4efba1afeddada31b00f'), sysdate(), sysdate());
