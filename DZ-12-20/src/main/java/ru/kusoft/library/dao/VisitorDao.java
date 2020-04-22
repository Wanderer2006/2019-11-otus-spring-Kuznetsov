package ru.kusoft.library.dao;

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
}
