package ru.kusoft.library.dao;

import ru.kusoft.library.dao.ext.Relation;
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

    long countBookAtVisitor(long id);

    boolean existBookAtVisitor(long id);

    long countVisitorWithBook(long id);

    boolean existVisitorWithBook(long id);

    void addBookForVisitor(long bookId, long visitorId);

    void deleteBookForVisitor(long bookId, long visitorId);
}
