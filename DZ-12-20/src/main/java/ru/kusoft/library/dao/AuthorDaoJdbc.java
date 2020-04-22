package ru.kusoft.library.dao;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Data
@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public long count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from authors", Long.class
        );
    }

    @Override
    public long insert(Author author) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        ps.addValue("last_name", author.getLastName());
        ps.addValue("first_name", author.getFirstName());
        ps.addValue("second_name", isNotBlank(author.getSecondName()) ? author.getSecondName() : null);
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into authors (last_name, first_name, second_name) " +
                        "values (:last_name, :first_name, :second_name)", ps, kh
        );
        return (long) kh.getKey();
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from authors where author_id = :id", params, new AuthorMapper()
        );
    }

    @Override
    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from authors where author_id = :id", params, Long.class
        ) > 0;
    }

    @Override
    public Author getByAuthor(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("last_name", author.getLastName().toUpperCase());
        params.addValue("first_name", author.getFirstName().toUpperCase());
        String addSecondNameInQuery = "";
        if (isNotBlank(author.getSecondName())) {
            params.addValue("second_name", author.getSecondName().toUpperCase());
            addSecondNameInQuery = " and upper(second_name) = :second_name";
        }
        return namedParameterJdbcOperations.queryForObject(
                "select * from authors where upper(last_name) = :last_name and upper(first_name) = :first_name"
                        + addSecondNameInQuery, params, new AuthorMapper()
        );
    }

    @Override
    public boolean existAuthor(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("last_name", author.getLastName().toUpperCase());
        params.addValue("first_name", author.getFirstName().toUpperCase());
        String addSecondNameInQuery = "";
        if (isNotBlank(author.getSecondName())) {
            params.addValue("second_name", author.getSecondName().toUpperCase());
            addSecondNameInQuery = " and upper(second_name) = :second_name";
        }
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from authors where upper(last_name) = :last_name and upper(first_name) = :first_name"
                        + addSecondNameInQuery, params, Long.class
        ) > 0;
    }


    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query(
                "select * from authors", new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAllUsed() {
        return namedParameterJdbcOperations.query(
                "select a.author_id, a.last_name, a.first_name, a.second_name " +
                        "from (authors a inner join book_author ba on a.author_id = ba.right_id) " +
                        "group by a.author_id, a.last_name, a.first_name, a.second_name", new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAuthorsByBookId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.query(
                "select a.author_id, a.last_name, a.first_name, a.second_name " +
                        "from (authors a left join book_author ba on a.author_id = ba.right_id) " +
                        "where left_id = :id", params, new AuthorMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from authors where author_id = :id", params
        );
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("author_id");
            String firstName = resultSet.getString("last_name");
            String lastName = resultSet.getString("first_name");
            String secondName = resultSet.getString("second_name");
            return new Author(id, firstName, lastName, secondName);
        }
    }
}
