package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Author;
import ru.kusoft.library.domain.Relation;
import ru.kusoft.library.domain.Visitor;

import java.util.List;

public interface VisitorDao {

    long count();

    long insert(Visitor visitor);

    Visitor getById(long id);

    boolean existById(long id);

    Visitor getByVisitor(Visitor visitor);

    boolean existVisitor(Visitor visitor);

    List<Visitor> getAll();

    List<Visitor> getAllUsed();

    List<Visitor> getVisitorsByBookId(long id);

    void deleteById(long id);

    List<Relation> getAllRelations();

    long countRelationById(long id);

    boolean existRelationById(long id);

    long countRelationByBookId(long id);

    boolean existRelationByBookId(long id);

    void insertRelation(long bookId, long visitorId);

    void deleteRelation(long bookId, long visitorId);
}
