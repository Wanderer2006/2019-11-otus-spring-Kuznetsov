insert into publishers (publisher_name)
values ('Диалектика'), ('Питер'), ('Вильямс'), ('АСТ'), ('Азбука-Аттикус'), ('Эксмо'), ('Фламинго');

insert into genres (genre)
values ('Повесть'), ('Роман'), ('Учебная литература'), ('Фантастика'), ('Компьютерная литература'), ('Детектив');

insert into authors (last_name, first_name)
values ('Бейли', 'Линн'), ('Хорстман', 'Кей'), ('Эккель', 'Брюс'), ('Сиерра', 'Кэти'), ('Бейтс', 'Берт');
insert into authors (last_name, first_name, second_name)
values ('Глуховский', 'Дмитрий', 'Алексеевич'), ('Гуляковский', 'Евгений', 'Яковлевич'), ('Ефремов', 'Иван', 'Антонович');

insert into visitors (last_name, first_name, second_name, age) 
values ('Иванов', 'Иван', 'Иванович', 30), ('Петров', 'Петр', 'Петрович', 18), ('Кузнецова', 'Дарья', 'Андреевна', 12), ('Сидоров', 'Сидор', 'Сидорович', 10), ('Кузьмин', 'Кузьма', 'Кузьмич', 100);

insert into books (title, copies,  publisher_id, year_publishing, printing, age_limit)
values ('Философия Java', 2, 2, 2019, 3000, 12), ('Изучаем Java', 2, 6, 2018, 2000, 12), ('Java. Библиотека профессионала. Том 1. Основы', 2, 3, 2016, 2000, 12), 
       ('Java. Библиотека профессионала. Том 2. Расширенные средства программирования', 2, 1, 2017, 2000, 12), ('Метро 2033', 2, 4, 2019, 3000, 18), 
       ('Метро 2034', 2, 4, 2019, 3000, 18), ('Метро 2035', 2, 4, 2019, 3000, 18), ('Сезон туманов', 2, 5, 2016, 1000, 12), ('Чужие пространства', 2, 5, 2017, 1500, 12), 
       ('Планета для контакта', 2, 6, 2009, 2000, 12), ('Изучаем SQL', 2, 2, 2012, 2000, 12), ('Туманность Андромеды', 2, 6, 1957, 1000, 12);

insert into book_genre (left_id, right_id)
values (1, 3), (1, 5), 
       (2, 3), (2, 5), 
       (3, 3), (3, 5), 
       (4, 3), (4, 5), 
       (5, 4), (5, 2), 
       (6, 4), (6, 2), 
       (7, 4), (7, 2), 
       (8, 4), (8, 2), 
       (9, 4), (9, 2), 
       (10, 4), (10, 1), 
       (11, 3), (11, 5);

insert into book_author (left_id, right_id)
values (1, 3), 
       (2, 4), (2, 5), 
       (3, 2), 
       (4, 2), 
       (5, 6), 
       (6, 6),
       (7, 6), 
       (8, 7), 
       (9, 7), 
       (10, 7), 
       (11, 1);

insert into visitor_book (left_id, right_id)
values (1, 1), (1, 3), (1, 5),
       (2, 5), (2, 7), (2, 9),
       (3, 8), (3, 11), (4, 11);
