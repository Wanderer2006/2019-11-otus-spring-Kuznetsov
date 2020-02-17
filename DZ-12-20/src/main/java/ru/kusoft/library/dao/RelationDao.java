package ru.kusoft.library.dao;

import ru.kusoft.library.domain.Relation;

import java.util.List;

public interface RelationDao {

    void setNameRelationTable(String nameRelationTable);

    long count();

    long countByLeftId(long id);

    long countByRightId(long id);

    void insert(Relation relation);

    Relation getByLeftId(long id);

    Relation getByRightId(long id);

    List<Relation> getAll();

    void delete(Relation relation);
}
