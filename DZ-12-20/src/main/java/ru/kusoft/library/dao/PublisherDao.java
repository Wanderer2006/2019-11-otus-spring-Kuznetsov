package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Book;
import ru.kusoft.library.domain.Publisher;

import java.util.List;

public interface PublisherDao {

    long count();

    long insert(Publisher publisher);

    Publisher getById(long id);

    boolean existById(long id);

    Publisher getByName(String name);

    boolean existByName(String name);

    List<Publisher> getAll();

    void deleteById(long id);

    boolean existRelationById(long id);

    long countRelationById(long id);
}
