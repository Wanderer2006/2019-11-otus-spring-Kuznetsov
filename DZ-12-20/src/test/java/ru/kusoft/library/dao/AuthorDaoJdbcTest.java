package ru.kusoft.library.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.kusoft.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с авторами должен ")
@JdbcTest
@Import({AuthorDaoJdbc.class})
class AuthorDaoJdbcTest {

    private static final int EXPECTED_AUTHORS_COUNT = 8;
    private static final int EXPECTED_USED_AUTHORS_COUNT = 7;
    private static final String NEW_AUTHOR_LAST_NAME = "Авторов";
    private static final String NEW_AUTHOR_FIRST_NAME = "Автор";
    private static final String NEW_AUTHOR_SECOND_NAME = "Авторович";
    private static final long NEW_AUTHOR_ID = 9L;
    private static final long DEFAULT_AUTHOR_ID = 7L;
    private static final String DEFAULT_AUTHOR_LAST_NAME = "Гуляковский";
    private static final String DEFAULT_AUTHOR_FIRST_NAME = "Евгений";
    private static final String DEFAULT_AUTHOR_SECOND_NAME = "Яковлевич";
    private static final String FOREIGN_AUTHOR_LAST_NAME = "Хорстман";
    private static final String FOREIGN_AUTHOR_FIRST_NAME = "Кей";
    private static final long LINKED_BOOK_ID = 2L;
    private static final int EXPECTED_LINKED_AUTHORS_COUNT = 2;
    private static final int EXPECTED_RELATIONS_COUNT = 12;

    @Autowired
    private AuthorDaoJdbc jdbc;

    @DisplayName("возвращать ожидаемое количество авторов в БД")
    @Test
    void shouldReturnExpectedAuthorCount() {
        assertThat(jdbc.count()).isEqualTo(EXPECTED_AUTHORS_COUNT);
    }

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        val author = new Author(NEW_AUTHOR_ID, NEW_AUTHOR_LAST_NAME, NEW_AUTHOR_FIRST_NAME, NEW_AUTHOR_SECOND_NAME);
        jdbc.insert(author);
        val actual = jdbc.getById(NEW_AUTHOR_ID);
        assertThat(actual).isEqualToComparingFieldByField(author);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        val actual = jdbc.getById(DEFAULT_AUTHOR_ID);
        assertThat(actual.getLastName()).isEqualTo(DEFAULT_AUTHOR_LAST_NAME);
    }

    @DisplayName("подтверждать наличие в БД автора по его id")
    @Test
    void shouldReturnSignExistenceAuthorById() {
        val actual = jdbc.existById(DEFAULT_AUTHOR_ID);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД отсутствующего автора по его id")
    @Test
    void shouldReturnSignNonExistenceAuthorById() {
        val actual = jdbc.existById(NEW_AUTHOR_ID);
        assertThat(actual).isFalse();
    }

    @DisplayName("возвращать ожидаемого автора по его ФИО")
    @Test
    void shouldReturnExpectedAuthorByFIO() {
        val author = new Author(DEFAULT_AUTHOR_ID, DEFAULT_AUTHOR_LAST_NAME, DEFAULT_AUTHOR_FIRST_NAME, DEFAULT_AUTHOR_SECOND_NAME);
        val actual = jdbc.getByAuthor(author);
        assertThat(actual).isEqualToComparingFieldByField(author);
    }

    @DisplayName("возвращать ожидаемого автора по его ФИ")
    @Test
    void shouldReturnExpectedAuthorByFI() {
        val author = new Author(null, FOREIGN_AUTHOR_LAST_NAME, FOREIGN_AUTHOR_FIRST_NAME, null);
        val actual = jdbc.getByAuthor(author);
        assertThat(actual.getLastName()).isEqualTo(FOREIGN_AUTHOR_LAST_NAME);
    }

    @DisplayName("подтверждать наличие в БД автора по его ФИО")
    @Test
    void shouldReturnSignExistenceAuthorByFIO() {
        val author = new Author(null, DEFAULT_AUTHOR_LAST_NAME, DEFAULT_AUTHOR_FIRST_NAME, DEFAULT_AUTHOR_SECOND_NAME);
        boolean actual = jdbc.existAuthor(author);
        assertThat(actual).isTrue();
    }

    @DisplayName("подтверждать наличие в БД автора по его ФИ")
    @Test
    void shouldReturnSignExistenceAuthorByFI() {
        val author = new Author(null, FOREIGN_AUTHOR_LAST_NAME, FOREIGN_AUTHOR_FIRST_NAME, null);
        val actual = jdbc.existAuthor(author);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД автора по его ФИО")
    @Test
    void shouldReturnSignNonExistenceAuthorByFIO() {
        val author = new Author(null, NEW_AUTHOR_LAST_NAME, NEW_AUTHOR_FIRST_NAME, NEW_AUTHOR_SECOND_NAME);
        val actual = jdbc.existAuthor(author);
        assertThat(actual).isFalse();
    }

    @DisplayName("загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        val authors = jdbc.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_AUTHORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""));
    }

    @DisplayName("загружать список авторов, привязанных к книгам")
    @Test
    void shouldReturnCorrectLinkedAuthorsList() {
        val authors = jdbc.getAllUsed();
        assertThat(authors).isNotNull().hasSize(EXPECTED_USED_AUTHORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""));
    }

    @DisplayName("загружать список авторов, привязанных к книге с конкретным id")
    @Test
    void shouldReturnCorrectLinkedAuthorsListByBookId() {
        val authors = jdbc.getAuthorsByBookId(LINKED_BOOK_ID);
        assertThat(authors).isNotNull().hasSize(EXPECTED_LINKED_AUTHORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""))
                .anyMatch(a -> a.getLastName().equals("Сиерра") && a.getFirstName().equals("Кэти"))
                .anyMatch(a -> a.getLastName().equals("Бейтс") && a.getFirstName().equals("Берт"));
    }

    @DisplayName("удалять в БД автора по его id (на автора не должно быть ссылок в таблице book_author)")
    @Test
    void shouldDeleteAuthorById() {
        jdbc.deleteById(EXPECTED_AUTHORS_COUNT);
        assertThat(jdbc.existById(EXPECTED_AUTHORS_COUNT)).isFalse();
    }

    @DisplayName("загружать список всех связей из таблицы book_author")
    @Test
    void shouldReturnCorrectRelationList() {
        val relations = jdbc.getAllRelations();
        assertThat(relations).isNotNull().hasSize(EXPECTED_RELATIONS_COUNT);
    }

    @DisplayName("подтверждать наличие связей автора по его id с книгами в таблице book_author")
    @Test
    void shouldReturnSignExistenceRelationByAuthorId() {
        val actual = jdbc.existBooksForAuthor(DEFAULT_AUTHOR_ID);
        assertThat(actual).isTrue();
    }

}