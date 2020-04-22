package ru.kusoft.library.service;

import ru.kusoft.library.dao.ext.Relation;

public interface BookService {

    void showAllBooksShortInfo();

    void showAllBooksFullInfo();

    void deleteBookById(Long id);

    void giveOutBook(Relation relation);

    void returnBook(Relation relation);

    void addNewBook();
}
