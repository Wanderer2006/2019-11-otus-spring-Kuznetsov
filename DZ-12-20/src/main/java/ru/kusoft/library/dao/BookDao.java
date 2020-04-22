package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Book;

import java.util.List;

public interface BookDao {

    long count();

    boolean existById(long id);

    long insert(Book book);

    Book getById(long id);

    Book getByIdFullInfo(long id);

    List<Book> getAllShortInfo();

    List<Book> getAllFullInfo();

    void deleteById(long id);

    void addAuthorForBook(long bookId, long authorId);

    void deleteAuthorForBook(long bookId, long authorId);

    void addGenreForBook(long bookId, long genreId);

    void deleteGenreForBook(long bookId, long genreId);
}
