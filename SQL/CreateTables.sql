drop table if exists users;
drop table if exists user;
drop table if exists person;
drop table if exists event;
drop table if exists authtoken;

create table users
(
    username varchar(255) not null unique,
    password varchar(255) not null,
    email varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    gender char(1) not null,
    personID varchar (255) not null
);

create table person
(
    personID varchar(255) not null unique,
    associatedUsername varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    gender char(1) not null,
    fatherID varchar(255),
    motherID varchar(255),
    spouseID varchar(255)
);

create table event
(
    eventID varchar(255) not null unique,
    associatedUsername varchar(255) not null,
    personID varchar(255) not null,
    latitude float not null,
    longitude float not null,
    country varchar(255) not null,
    city varchar(255) not null,
    eventType varchar(255) not null,
    year integer not null
);

create table authtoken
(
    authtoken varchar(255) not null unique,
    username varchar(255) not null
);
