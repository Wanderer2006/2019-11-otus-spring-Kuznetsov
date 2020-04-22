package ru.kusoft.library.dao;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.domain.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Repository
public class PublisherDaoJdbc implements PublisherDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public long count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from publishers", Long.class
        );
    }

    @Override
    public long insert(Publisher publisher) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        ps.addValue("publisher_name", publisher.getPublisherName());
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into publishers (publisher_name) values (:publisher_name)", ps, kh
        );
        return (long) kh.getKey();
    }

    @Override
    public Publisher getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from publishers where publisher_id = :id", params, new PublisherMapper()
        );
    }

    @Override
    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from publishers where publisher_id = :id", params, Long.class
        ) > 0;
    }

    @Override
    public Publisher getByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name.toUpperCase());
        return namedParameterJdbcOperations.queryForObject(
                "select * from publishers where upper(publisher_name) = :name", params, new PublisherMapper()
        );
    }

    @Override
    public boolean existByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name.toUpperCase());
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from publishers where upper(publisher_name) = :name", params, Long.class
        ) > 0;
    }

    @Override
    public List<Publisher> getAll() {
        return namedParameterJdbcOperations.query(
                "select * from publishers", new PublisherMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from publishers where publisher_id = :id", params
        );
    }

    @Override
    public boolean existRelationById(long id) {
        return countRelationById(id) > 0;
    }

    @Override
    public long countRelationById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from books where publisher_id = :id", params, Long.class
        );
    }


    private static class PublisherMapper implements RowMapper<Publisher> {

        @Override
        public Publisher mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("publisher_id");
            String publisher_name = resultSet.getString("publisher_name");
            return new Publisher(id, publisher_name);
        }
    }
}
