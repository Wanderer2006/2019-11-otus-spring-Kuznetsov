package ru.kusoft.library.dao;

import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Author;
import ru.kusoft.library.dao.ext.Relation;

import java.util.List;

public interface AuthorDao {

    long count();

    long insert(Author author);

    Author getById(long id);

    boolean existById(long id);

    Author getByAuthor(Author author);

    boolean existAuthor(Author author);

    List<Author> getAll();

    List<Author> getAllUsed();

    List<Author> getAuthorsByBookId(long id);

    void deleteById(long id);

    List<Relation> getAllRelations();

    boolean existBooksForAuthor(long id);

    RelationHelper getBookAuthorRelation();
}
