package ru.kusoft.library.dao;

import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Genre;
import ru.kusoft.library.dao.ext.Relation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Getter
    private final RelationHelper bookGenreRelation;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookGenreRelation = new RelationHelper(namedParameterJdbcOperations, "book_genre");
    }

    @Override
    public long count() {
        return namedParameterJdbcOperations.getJdbcOperations().queryForObject(
                "select count(*) from genres", Long.class
        );
    }

    @Override
    public long insert(Genre genre) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        ps.addValue("genre", genre.getGenre());
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into genres (genre) values (:genre)", ps, kh
        );
        return (long) kh.getKey();
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from genres where genre_id = :id", params, new GenreMapper()
        );
    }

    @Override
    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from genres where genre_id = :id", params, Long.class
        ) > 0;
    }

    @Override
    public Genre getByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name.toUpperCase());
        return namedParameterJdbcOperations.queryForObject(
                "select * from genres where upper(genre) = :name", params, new GenreMapper()
        );
    }

    @Override
    public boolean existByName(String name) {
        Map<String, Object> params = Collections.singletonMap("name", name.toUpperCase());
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from genres where upper(genre) = :name", params, Long.class
        ) > 0;
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query(
                "select * from genres", new GenreMapper()
        );
    }

    @Override
    public List<Genre> getAllUsed() {
        return namedParameterJdbcOperations.query(
                "select g.genre_id, g.genre " +
                        "from (genres g inner join book_genre bg on g.genre_id = bg.right_id) " +
                        "group by g.genre_id, g.genre", new GenreMapper()
        );
    }

    @Override
    public List<Genre> getGenresByBookId(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.query(
                "select * from (genres g left join book_genre bg on g.genre_id = bg.right_id) where left_id = :id",
                params, new GenreMapper()
        );
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from genres where genre_id = :id", params
        );
    }

    @Override
    public List<Relation> getAllRelations() {
        return bookGenreRelation.getAll();
    }

    @Override
    public boolean existBooksForGenre(long id) {
        return bookGenreRelation.countByRightId(id) > 0;
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("genre_id");
            String genre = resultSet.getString("genre");
            return new Genre(id, genre);
        }
    }

}
