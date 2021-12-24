create schema messages;
create table registration_msg
(
    id   varchar(100) ,
    body varchar(500) not null,
    create_day timestamp,
    constraint registration_msg_pk
        primary key (id)
);

create table event_msg
(
    id   varchar(100) ,
    body varchar(500) not null,
    update_day timestamp,
    constraint event_msg_pk
        primary key (id)
);
