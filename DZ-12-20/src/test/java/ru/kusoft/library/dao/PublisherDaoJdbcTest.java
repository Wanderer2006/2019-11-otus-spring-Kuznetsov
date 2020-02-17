package ru.kusoft.library.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.kusoft.library.domain.Publisher;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с издательствами должен ")
@JdbcTest
@Import({PublisherDaoJdbc.class})
class PublisherDaoJdbcTest {

    private static final int EXPECTED_PUBLISHERS_COUNT = 7;
    private static final long NEW_PUBLISHER_ID = 8L;
    private static final String NEW_PUBLISHER_NAME = "Просвещение";
    private static final long DEFAULT_PUBLISHER_ID = 4L;
    private static final String DEFAULT_PUBLISHER_NAME = "АСТ";
    private static final int EXPECTED_COUNT_FOR_DEFAULT_PUBLISHER = 3;

    @Autowired
    private PublisherDaoJdbc jdbc;

    @DisplayName("возвращать ожидаемое количество издательств в БД")
    @Test
    void shouldReturnExpectedPublisherCount() {
        assertThat(jdbc.count()).isEqualTo(EXPECTED_PUBLISHERS_COUNT);
    }

    @DisplayName("добавлять издательство в БД")
    @Test
    void shouldInsertPublisher() {
        val genre = new Publisher(NEW_PUBLISHER_ID, NEW_PUBLISHER_NAME);
        jdbc.insert(genre);
        val actual = jdbc.getById(NEW_PUBLISHER_ID);
        assertThat(actual).isEqualToComparingFieldByField(genre);
    }

    @DisplayName("возвращать ожидаемое издательство по его id")
    @Test
    void shouldReturnExpectedPublisherById() {
        val actual = jdbc.getById(DEFAULT_PUBLISHER_ID);
        assertThat(actual.getPublisherName()).isEqualTo(DEFAULT_PUBLISHER_NAME);
    }

    @DisplayName("подтверждать наличие в БД издательства по его id")
    @Test
    void shouldReturnSignExistencePublisherById() {
        val actual = jdbc.existById(DEFAULT_PUBLISHER_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД отсутствующего издательства по его id")
    @Test
    void shouldReturnSignNonExistencePublisherById() {
        val actual = jdbc.existById(NEW_PUBLISHER_ID);
        assertThat(actual).isFalse();
    }

    @DisplayName("возвращать ожидаемое издательство по его наименованию")
    @Test
    void shouldReturnExpectedPublisherByName() {
        val actual = jdbc.getByName(DEFAULT_PUBLISHER_NAME);
        assertThat(actual.getPublisherName()).isEqualTo(DEFAULT_PUBLISHER_NAME);
    }

    @DisplayName("подтверждать наличие в БД издательства по его наименованию")
    @Test
    void shouldReturnSignExistencePublisherByName() {
        boolean actual = jdbc.existByName(DEFAULT_PUBLISHER_NAME);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД издательства по его нименованию")
    @Test
    void shouldReturnSignNonExistencePublisherByName() {
        boolean actual = jdbc.existByName(NEW_PUBLISHER_NAME);
        assertThat(actual).isFalse();
    }

    @DisplayName("загружать список всех издательств")
    @Test
    void shouldReturnCorrectPublishersList() {
        val authors = jdbc.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_PUBLISHERS_COUNT)
                .allMatch(a -> !a.getPublisherName().equals(""));
    }

    @DisplayName("удалять в БД издательство по его id (на издательств не должно быть ссылок в таблице book)")
    @Test
    void shouldDeletePublisherById() {
        jdbc.deleteById(EXPECTED_PUBLISHERS_COUNT);
        assertThat(jdbc.existById(EXPECTED_PUBLISHERS_COUNT)).isFalse();
    }

    @DisplayName("подтверждать наличие связей издательства по его id с книгами в таблице book_genre")
    @Test
    void shouldReturnSignExistenceRelationByPublisherId() {
        val actual = jdbc.existRelationById(DEFAULT_PUBLISHER_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("возвращать ожидаемое кодличество связей для издательства по его id ")
    @Test
    void shouldReturnExpectedRelationCountByPublisherId() {
        val actual = jdbc.countRelationById(DEFAULT_PUBLISHER_ID);
        assertThat(actual).isEqualTo(EXPECTED_COUNT_FOR_DEFAULT_PUBLISHER);
    }

}