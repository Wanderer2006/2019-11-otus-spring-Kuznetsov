package ru.kusoft.library.dao;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kusoft.library.dao.ext.Relation;
import ru.kusoft.library.domain.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public long count() {
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from books", new HashMap<>(), Long.class
        );
    }

    @Override
    public long insert(Book book) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        ps.addValue("title", book.getTitle());
        ps.addValue("copies", book.getCopies());
        ps.addValue("publisher_id", book.getPublisher().getPublisherId());
        ps.addValue("year_publishing", book.getYearPublishing());
        ps.addValue("printing", book.getPrinting());
        ps.addValue("age_limit", book.getAgeLimit());
        KeyHolder kh = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (title, copies, publisher_id, year_publishing, printing, age_limit) " +
                        "values (:title, :copies, :publisher_id, :year_publishing, :printing, :age_limit)", ps, kh
        );
        book.getAuthors().forEach(a -> addAuthorForBook((long) kh.getKey(), a.getAuthorId()));
        book.getGenres().forEach(g -> addGenreForBook((long) kh.getKey(), g.getGenreId()));
        return (long) kh.getKey();
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select * from (books b natural join publishers) where book_id = :id", params, new BookMapper()
        );
    }

    public boolean existById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select count(*) from books where book_id = :id", params, Long.class
        ) > 0;
    }

    @Override
    public Book getByIdFullInfo(long id) {
        Book book = getById(id);
        if (book != null) {
            List<Book> books = new ArrayList<>();
            books.add(getById(id));
            mergeBooksWithFullInfo(books);
            return books.get(0);
        }

        return book;
    }

    @Override
    public List<Book> getAllShortInfo() {
        return namedParameterJdbcOperations.query(
                "select * from (books b natural join publishers p)", new BookMapper()
        );
    }

    @Override
    public List<Book> getAllFullInfo() {
        List<Book> books = getAllShortInfo();

        mergeBooksWithFullInfo(books);

        return books;
    }

    private void mergeBooksWithFullInfo(List<Book> books) {
        List<Author> authors = authorDao.getAllUsed();
        List<Relation> relationsBookAuthor = authorDao.getAllRelations();
        mergeBooksWithAuthors(books, authors, relationsBookAuthor);

        List<Genre> genres = genreDao.getAllUsed();
        List<Relation> relationsBookGenre = genreDao.getAllRelations();
        mergeBooksWithGenres(books, genres, relationsBookGenre);
    }

    private void mergeBooksWithAuthors(List<Book> books, List<Author> authors, List<Relation> relations) {
        Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getBookId, b -> b));
        Map<Long, Author> authorMap = authors.stream().collect(Collectors.toMap(Author::getAuthorId, a -> a));
        relations.forEach(r -> {
            if (bookMap.containsKey(r.getLeftId()) && authorMap.containsKey(r.getRightId())) {
                bookMap.get(r.getLeftId()).getAuthors().add(authorMap.get(r.getRightId()));
            }
        });
    }

    private void mergeBooksWithGenres(List<Book> books, List<Genre> genres, List<Relation> relations) {
        Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getBookId, b -> b));
        Map<Long, Genre> genreMap = genres.stream().collect(Collectors.toMap(Genre::getGenreId, g -> g));
        relations.forEach(r -> {
            if (bookMap.containsKey(r.getLeftId()) && genreMap.containsKey(r.getRightId())) {
                bookMap.get(r.getLeftId()).getGenres().add(genreMap.get(r.getRightId()));
            }
        });
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update(
                "delete from books where book_id = :id", params
        );
    }

    @Override
    public void addAuthorForBook(long bookId, long authorId) {
        authorDao.getBookAuthorRelation().insert(new Relation(bookId, authorId));
    }

    @Override
    public void deleteAuthorForBook(long bookId, long authorId) {
        authorDao.getBookAuthorRelation().delete(new Relation(bookId, authorId));
    }

    @Override
    public void addGenreForBook(long bookId, long genreId) {
        genreDao.getBookGenreRelation().insert(new Relation(bookId, genreId));
    }

    @Override
    public void deleteGenreForBook(long bookId, long genreId) {
        genreDao.getBookGenreRelation().delete(new Relation(bookId, genreId));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getInt("book_id");
            String title = resultSet.getString("title");
            int copies = resultSet.getInt("copies");
            String publisherName = resultSet.getString("publisher_name");
            long publisherId = resultSet.getInt("publisher_id");
            Publisher publisher = new Publisher(publisherId, publisherName);
            int yearPublishing = resultSet.getInt("year_publishing");
            int printing = resultSet.getInt("printing");
            int ageLimit = resultSet.getInt("age_limit");
            return new Book(id, title, copies, publisher, yearPublishing, printing, ageLimit,
                    new ArrayList<>(), new ArrayList<>());
        }
    }
}
