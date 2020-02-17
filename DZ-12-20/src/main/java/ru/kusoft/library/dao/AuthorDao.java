package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Author;
import ru.kusoft.library.domain.Relation;

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

    boolean existRelationById(long id);

    void insertRelation(long bookId, long authorId);

    void deleteRelation(long bookId, long authorId);
}
