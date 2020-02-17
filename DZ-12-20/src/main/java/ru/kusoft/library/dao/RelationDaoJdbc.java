package ru.kusoft.library.dao;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.domain.Relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Scope("prototype")
public class RelationDaoJdbc implements RelationDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    @Setter
    private String nameRelationTable;

    @Override
    public long count() {
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from " + nameRelationTable, new HashMap<>(0), Long.class
        );
    }

    @Override
    public long countByLeftId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from " + nameRelationTable + " where left_id = :id", params, Long.class
        );
    }

    @Override
    public long countByRightId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from " + nameRelationTable + " where right_id = :id", params, Long.class
        );
    }

    @Override
    public void insert(Relation relation) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("left_id", relation.getLeftId());
        params.addValue("right_id", relation.getRightId());
        namedParameterJdbcOperations.update(
                "insert into " + nameRelationTable + " (left_id, right_id) values (:left_id, :right_id)", params
        );
    }

    @Override
    public Relation getByLeftId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from " + nameRelationTable + " where left_id = :id", params, new RelationMapper()
        );
    }

    @Override
    public Relation getByRightId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from " + nameRelationTable + " where right_id = :id", params, new RelationMapper()
        );
    }

    @Override
    public List<Relation> getAll() {
        return namedParameterJdbcOperations.query(
                "select * from " + nameRelationTable, new HashMap<>(0), new RelationMapper()
        );
    }

    @Override
    public void delete(Relation relation) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("left_id", relation.getLeftId());
        params.addValue("right_id", relation.getRightId());
        namedParameterJdbcOperations.update(
                "delete from " + nameRelationTable + " where left_id = :left_id and right_id = :right_id", params
        );
    }

    private static class RelationMapper implements RowMapper<Relation> {

        @Override
        public Relation mapRow(ResultSet resultSet, int i) throws SQLException {
            long leftId = resultSet.getInt("left_id");
            long rightId = resultSet.getInt("right_id");
            return new Relation(leftId, rightId);
        }
    }
}
