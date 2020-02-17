drop table if exists publishers;
create table publishers(
    publisher_id bigint not null auto_increment primary key,
    publisher_name varchar(255) not null
);

drop table if exists books;
create table books(
    book_id bigint not null auto_increment primary key,
    title varchar(255) not null,
    copies int not null,
    publisher_id bigint references publishers (publisher_id),
    year_publishing int not null,
    printing int not null,
    age_limit int not null,
);

drop table if exists genres;
create table genres(
    genre_id bigint not null auto_increment primary key,
    genre varchar(255) not null
);

drop table if exists book_genre;
create table book_genre(
    left_id bigint references books (book_id) on delete cascade,
    right_id bigint references genres (genre_id),
);

drop table if exists authors;
create table authors(
    author_id bigint not null auto_increment primary key,
    last_name varchar(255) not null,
    first_name varchar(255) not null,
    second_name varchar(255)
);

drop table if exists book_author;
create table book_author(
    left_id bigint references books (book_id) on delete cascade,
    right_id bigint references authors (author_id),
);

drop table if exists visitors;
create table visitors(
    visitor_id bigint not null auto_increment primary key,
    last_name varchar(255) not null,
    first_name varchar(255) not null,
    second_name varchar(255) not null,
    age int not null
);

drop table if exists visitor_book;
create table visitor_book(
    left_id bigint references visitors (visitor_id),
    right_id bigint references books (book_id) on delete cascade,
);