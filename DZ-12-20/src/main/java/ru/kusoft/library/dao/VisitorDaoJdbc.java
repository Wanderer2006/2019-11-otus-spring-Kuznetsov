package ru.kusoft.library.dao;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.domain.Visitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Repository
public class VisitorDaoJdbc implements VisitorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public long count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from visitors", Long.class
        );
    }

    @Override
    public long insert(Visitor visitor) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        ps.addValue("last_name", visitor.getLastName());
        ps.addValue("first_name", visitor.getFirstName());
        ps.addValue("second_name", visitor.getSecondName());
        ps.addValue("age", visitor.getAge());
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into visitors (last_name, first_name, second_name, age) " +
                        "values (:last_name, :first_name, :second_name, :age)", ps, kh
        );
        return (long) kh.getKey();
    }

    @Override
    public Visitor getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from visitors where visitor_id = :id", params, new VisitorMapper()
        );
    }

    @Override
    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from visitors where visitor_id = :id", params, Long.class
        ) > 0;
    }

    @Override
    public Visitor getByVisitor(Visitor visitor) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("last_name", visitor.getLastName().toUpperCase());
        params.addValue("first_name", visitor.getFirstName().toUpperCase());
        params.addValue("second_name", visitor.getSecondName().toUpperCase());
        params.addValue("age", visitor.getAge());
        return namedParameterJdbcOperations.queryForObject(
                "select * from visitors " +
                        "where upper(last_name) = :last_name and upper(first_name) = :first_name " +
                        "and upper(second_name) = :second_name and age = :age", params, new VisitorMapper()
        );
    }

    @Override
    public boolean existVisitor(Visitor visitor) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("last_name", visitor.getLastName().toUpperCase());
        params.addValue("first_name", visitor.getFirstName().toUpperCase());
        params.addValue("second_name", visitor.getSecondName().toUpperCase());
        params.addValue("age", visitor.getAge());
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from visitors " +
                        "where upper(last_name) = :last_name and upper(first_name) = :first_name " +
                        "and upper(second_name) = :second_name and age = :age", params, Long.class
        ) > 0;
    }

    @Override
    public List<Visitor> getAll() {
        return namedParameterJdbcOperations.query(
                "select * from visitors", new VisitorMapper()
        );
    }

    @Override
    public List<Visitor> getAllUsed() {
        return namedParameterJdbcOperations.query(
                "select v.visitor_id, v.last_name, v.first_name, v.second_name, v.age " +
                        "from (visitors v inner join visitor_book vb on v.visitor_id = vb.left_id) " +
                        "group by v.visitor_id, v.last_name, v.first_name, v.second_name, v.age", new VisitorMapper()
        );
    }

    @Override
    public List<Visitor> getVisitorsByBookId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.query(
                "select * from (visitors v left join visitor_book vb on v.visitor_id = vb.left_id) " +
                        "where right_id = :id",
                params, new VisitorMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from visitors where visitor_id = :id", params
        );
    }

    private static class VisitorMapper implements RowMapper<Visitor> {

        @Override
        public Visitor mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("visitor_id");
            String firstName = resultSet.getString("last_name");
            String lastName = resultSet.getString("first_name");
            String secondName = resultSet.getString("second_name");
            int age = resultSet.getInt("age");
            return new Visitor(id, firstName, lastName, secondName, age);
        }
    }
}
