package ru.kusoft.library.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.kusoft.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами должен ")
@JdbcTest
@Import({GenreDaoJdbc.class, RelationDaoJdbc.class})
class GenreDaoJdbcTest {

    private static final int EXPECTED_GENRES_COUNT = 6;
    private static final long NEW_GENRE_ID = 7L;
    private static final String NEW_GENRE_NAME = "А не выдумать ли новый жанр";
    private static final long DEFAULT_GENRE_ID = 1L;
    private static final String DEFAULT_GENRE_NAME = "Повесть";
    private static final int EXPECTED_USED_GENRES_COUNT = 5;
    private static final long LINKED_BOOK_ID = 5L;
    private static final int EXPECTED_LINKED_GENRES_COUNT = 2;
    private static final int EXPECTED_RELATIONS_COUNT = 22;
    private static final long GENRE_ID_FOR_NEW_RELATION = 6L;
    private static final long BOOK_ID_FOR_NEW_RELATION = 12L;
    private static final long BOOK_ID_FOR_DELETE_RELATION = 10L;

    @Autowired
    private GenreDaoJdbc jdbc;

    @DisplayName("возвращать ожидаемое количество жанров в БД")
    @Test
    void shouldReturnExpectedGenreCount() {
        assertThat(jdbc.count()).isEqualTo(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        val genre = new Genre(NEW_GENRE_ID, NEW_GENRE_NAME);
        jdbc.insert(genre);
        val actual = jdbc.getById(NEW_GENRE_ID);
        assertThat(actual).isEqualToComparingFieldByField(genre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        val actual = jdbc.getById(DEFAULT_GENRE_ID);
        assertThat(actual.getGenre()).isEqualTo(DEFAULT_GENRE_NAME);
    }

    @DisplayName("подтверждать наличие в БД жанра по его id")
    @Test
    void shouldReturnSignExistenceGenreById() {
        val actual = jdbc.existById(DEFAULT_GENRE_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД отсутствующего жанра по его id")
    @Test
    void shouldReturnSignNonExistenceGenreById() {
        val actual = jdbc.existById(NEW_GENRE_ID);
        assertThat(actual).isFalse();
    }

    @DisplayName("возвращать ожидаемый жанр по его наименованию")
    @Test
    void shouldReturnExpectedGenreByName() {
        val actual = jdbc.getByName(DEFAULT_GENRE_NAME);
        assertThat(actual.getGenre()).isEqualTo(DEFAULT_GENRE_NAME);
    }

    @DisplayName("подтверждать наличие в БД жанра по его наименованию")
    @Test
    void shouldReturnSignExistenceGenreByName() {
        boolean actual = jdbc.existByName(DEFAULT_GENRE_NAME);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД жанра по его нименованию")
    @Test
    void shouldReturnSignNonExistenceGenreByName() {
        boolean actual = jdbc.existByName(NEW_GENRE_NAME);
        assertThat(actual).isFalse();
    }

    @DisplayName("загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        val authors = jdbc.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .allMatch(a -> !a.getGenre().equals(""));
    }

    @DisplayName("загружать список жанров, привязанных к книгам")
    @Test
    void shouldReturnCorrectLinkedGenresList() {
        val authors = jdbc.getAllUsed();
        assertThat(authors).isNotNull().hasSize(EXPECTED_USED_GENRES_COUNT)
                .allMatch(a -> !a.getGenre().equals(""));
    }

    @DisplayName("загружать список жанров, привязанных к книге с конкретным id")
    @Test
    void shouldReturnCorrectLinkedGenresListByBookId() {
        val authors = jdbc.getGenresByBookId(LINKED_BOOK_ID);
        assertThat(authors).isNotNull().hasSize(EXPECTED_LINKED_GENRES_COUNT)
                .allMatch(a -> !a.getGenre().equals(""))
                .anyMatch(a -> a.getGenre().equals("Фантастика"))
                .anyMatch(a -> a.getGenre().equals("Роман"));
    }

    @DisplayName("удалять в БД жанр по его id (на жанр не должно быть ссылок в таблице book_genre)")
    @Test
    void shouldDeleteGenreById() {
        jdbc.deleteById(EXPECTED_GENRES_COUNT);
        assertThat(jdbc.existById(EXPECTED_GENRES_COUNT)).isFalse();
    }

    @DisplayName("загружать список всех связей из таблицы book_genre")
    @Test
    void shouldReturnCorrectRelationList() {
        val relations = jdbc.getAllRelations();
        assertThat(relations).isNotNull().hasSize(EXPECTED_RELATIONS_COUNT);
    }

    @DisplayName("подтверждать наличие связей жанра по его id с книгами в таблице book_genre")
    @Test
    void shouldReturnSignExistenceRelationByGenreId() {
        val actual = jdbc.existRelationById(DEFAULT_GENRE_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("добавлять связь между жанром и книгой в таблицу book_genre")
    @Test
    void shouldAddRelationByBookIdAndGenreId() {
        val current = jdbc.existRelationById(GENRE_ID_FOR_NEW_RELATION);
        assertThat(current).isFalse();
        jdbc.insertRelation(BOOK_ID_FOR_NEW_RELATION, GENRE_ID_FOR_NEW_RELATION);
        val actual = jdbc.existRelationById(GENRE_ID_FOR_NEW_RELATION);
        assertThat(actual).isTrue();
    }

    @DisplayName("удалять связь между жанром и книгой в таблице book_genre")
    @Test
    void shouldDeleteRelationByBookIdAndGenreId() {
        val current = jdbc.existRelationById(DEFAULT_GENRE_ID);
        assertThat(current).isTrue();
        jdbc.deleteRelation(BOOK_ID_FOR_DELETE_RELATION, DEFAULT_GENRE_ID);
        val actual = jdbc.existRelationById(DEFAULT_GENRE_ID);
        assertThat(actual).isFalse();
    }

}