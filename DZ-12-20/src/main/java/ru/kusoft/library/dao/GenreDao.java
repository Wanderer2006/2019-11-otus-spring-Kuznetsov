package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Genre;
import ru.kusoft.library.domain.Relation;

import java.util.List;

public interface GenreDao {

    long count();

    long insert(Genre genre);

    Genre getById(long id);

    boolean existById(long id);

    Genre getByName(String name);

    boolean existByName(String name);

    List<Genre> getAll();

    List<Genre> getAllUsed();

    List<Genre> getGenresByBookId(long id);

    void deleteById(long id);

    List<Relation> getAllRelations();

    boolean existRelationById(long id);

    void insertRelation(long bookId, long authorId);

    void deleteRelation(long bookId, long genreId);
}
