package ru.kusoft.library.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.kusoft.library.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с посетителями должен ")
@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class, RelationDaoJdbc.class})
class BookDaoJdbcTest {

    private static final int EXPECTED_BOOKS_COUNT = 12;
    private static final long NEW_BOOK_ID = 13L;
    private static final String NEW_BOOK_TITLE = "Русские народные сказки";
    private static final int NEW_BOOK_COPIES = 10;
    private static final long NEW_PUBLISHER_ID = 1L;
    private static final String NEW_PUBLISHER_NAME = "Диалектика";
    private static final int NEW_BOOK_YEAR_PUBLISHING = 1500;
    private static final int NEW_BOOK_PRINTING = 100500;
    private static final int NEW_BOOK_AGE_LIMIT = 1;
    private static final long DEFAULT_BOOK_ID = 1L;
    private static final String DEFAULT_BOOK_TITLE = "Философия Java";


    @Autowired
    private BookDaoJdbc jdbc;

    @DisplayName("возвращать ожидаемое количество книг в БД")
    @Test
    void shouldReturnExpectedBookCount() {
        assertThat(jdbc.count()).isEqualTo(EXPECTED_BOOKS_COUNT);
    }

    @DisplayName("добавлять книгу в БД")
    @Test
    void shouldInsertBook() {
        val publisher = new Publisher(NEW_PUBLISHER_ID, NEW_PUBLISHER_NAME);
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        val book = new Book(NEW_BOOK_ID, NEW_BOOK_TITLE, NEW_BOOK_COPIES, publisher,
                NEW_BOOK_YEAR_PUBLISHING, NEW_BOOK_PRINTING, NEW_BOOK_AGE_LIMIT, authors, genres);
        jdbc.insert(book);
        val actual = jdbc.getById(NEW_BOOK_ID);
        assertThat(actual).isEqualToComparingFieldByField(book);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        val actual = jdbc.getById(DEFAULT_BOOK_ID);
        assertThat(actual.getTitle()).isEqualTo(DEFAULT_BOOK_TITLE);
    }

    @DisplayName("подтверждать наличие в БД книги по ее id")
    @Test
    void shouldReturnSignExistenceBookById() {
        val actual = jdbc.existById(DEFAULT_BOOK_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("возвращать ожидаемую книгу с полной информацией по её id")
    @Test
    void shouldReturnExpectedBookFullInfoById() {
        val actual = jdbc.getByIdFullInfo(DEFAULT_BOOK_ID);
        assertThat(actual)
                .matches(b -> b.getTitle().equals(DEFAULT_BOOK_TITLE))
                .matches(b -> b.getAuthors().stream()
                        .anyMatch(a -> a.getLastName().equals("Эккель") && a.getFirstName().equals("Брюс")))
                .matches(b -> b.getGenres().stream()
                        .allMatch(g -> g.getGenre().equals("Учебная литература") ||
                                g.getGenre().equals("Компьютерная литература")));
    }

    @DisplayName("возвращать полный список книг с краткой информацией по ним")
    @Test
    void shouldReturnCorrectBookListWithShortInfo() {
        val actual = jdbc.getAllShortInfo();
        assertThat(actual).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthors().isEmpty())
                .allMatch(b -> b.getGenres().isEmpty());
    }

    @DisplayName("возвращать полный список книг с полной информацией по ним")
    @Test
    void shouldReturnCorrectBookListWithFullInfo() {
        val actual = jdbc.getAllFullInfo();
        assertThat(actual).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .filteredOn(b -> !b.getTitle().equals("Туманность Андромеды"))
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> !b.getAuthors().isEmpty())
                .allMatch(b -> !b.getGenres().isEmpty());
    }

    @DisplayName("удалять в БД книгу по её id (на кнгигу не должно быть ссылок многие-ко-многим)")
    @Test
    void shouldDeleteVisitorById() {
        jdbc.deleteById(EXPECTED_BOOKS_COUNT);
        assertThat(jdbc.existById(EXPECTED_BOOKS_COUNT)).isFalse();
    }

}