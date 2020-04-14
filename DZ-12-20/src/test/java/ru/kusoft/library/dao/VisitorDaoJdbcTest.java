package ru.kusoft.library.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.kusoft.library.domain.Visitor;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с посетителями должен ")
@JdbcTest
@Import({VisitorDaoJdbc.class})
class VisitorDaoJdbcTest {

    private static final int EXPECTED_VISITORS_COUNT = 5;
    private static final long NEW_VISITOR_ID = 6L;
    private static final String NEW_VISITOR_LAST_NAME = "Посетителев";
    private static final String NEW_VISITOR_FIRST_NAME = "Посетитель";
    private static final String NEW_VISITOR_SECOND_NAME = "Посетителевич";
    private static final int NEW_VISITOR_AGE = 12;
    private static final long DEFAULT_VISITOR_ID = 1L;
    private static final String DEFAULT_VISITOR_LAST_NAME = "Иванов";
    private static final String DEFAULT_VISITOR_FIRST_NAME = "Иван";
    private static final String DEFAULT_VISITOR_SECOND_NAME = "Иванович";
    private static final int DEFAULT_VISITOR_AGE = 30;
    private static final int EXPECTED_USED_VISITORS_COUNT = 4;
    private static final long LINKED_BOOK_ID = 5L;
    private static final int EXPECTED_LINKED_VISITORS_COUNT = 2;

    @Autowired
    private VisitorDaoJdbc jdbc;

    @DisplayName("возвращать ожидаемое количество посетителей в БД")
    @Test
    void shouldReturnExpectedVisitorCount() {
        assertThat(jdbc.count()).isEqualTo(EXPECTED_VISITORS_COUNT);
    }

    @DisplayName("добавлять посетителя в БД")
    @Test
    void shouldInsertVisitor() {
        val visitor = new Visitor(NEW_VISITOR_ID, NEW_VISITOR_LAST_NAME, NEW_VISITOR_FIRST_NAME,
                NEW_VISITOR_SECOND_NAME, NEW_VISITOR_AGE);
        jdbc.insert(visitor);
        val actual = jdbc.getById(NEW_VISITOR_ID);
        assertThat(actual).isEqualToComparingFieldByField(visitor);
    }

    @DisplayName("возвращать ожидаемого посетителя по его id")
    @Test
    void shouldReturnExpectedVisitorById() {
        val actual = jdbc.getById(DEFAULT_VISITOR_ID);
        assertThat(actual.getLastName()).isEqualTo(DEFAULT_VISITOR_LAST_NAME);
    }

    @DisplayName("возвращать ожидаемого посетителя по его ФИО и возрасту")
    @Test
    void shouldReturnExpectedVisitorByFIOAndAge() {
        val visitor = new Visitor(DEFAULT_VISITOR_ID, DEFAULT_VISITOR_LAST_NAME, DEFAULT_VISITOR_FIRST_NAME,
                DEFAULT_VISITOR_SECOND_NAME, DEFAULT_VISITOR_AGE);
        val actual = jdbc.getByVisitor(visitor);
        assertThat(actual).isEqualToComparingFieldByField(visitor);
    }

    @DisplayName("подтверждать наличие в БД посетителя по его ФИО")
    @Test
    void shouldReturnSignExistenceVisitorByFIO() {
        val visitor = new Visitor(DEFAULT_VISITOR_ID, DEFAULT_VISITOR_LAST_NAME, DEFAULT_VISITOR_FIRST_NAME,
                DEFAULT_VISITOR_SECOND_NAME, DEFAULT_VISITOR_AGE);
        boolean actual = jdbc.existVisitor(visitor);
        assertThat(actual).isTrue();
    }

    @DisplayName("не подтверждать наличие в БД посетителя по его ФИО")
    @Test
    void shouldReturnSignNonExistenceVisitorByFIO() {
        val visitor = new Visitor(null, NEW_VISITOR_LAST_NAME, NEW_VISITOR_FIRST_NAME, NEW_VISITOR_SECOND_NAME,
                NEW_VISITOR_AGE);
        val actual = jdbc.existVisitor(visitor);
        assertThat(actual).isFalse();
    }

    @DisplayName("загружать список всех посетителей")
    @Test
    void shouldReturnCorrectVisitorsList() {
        val authors = jdbc.getAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_VISITORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""))
                .allMatch(a -> !a.getSecondName().equals(""))
                .allMatch(a -> !(a.getAge() == 0));
    }

    @DisplayName("загружать список посетителей, привязанных к книгам")
    @Test
    void shouldReturnCorrectLinkedVisitorsList() {
        val authors = jdbc.getAllUsed();
        assertThat(authors).isNotNull().hasSize(EXPECTED_USED_VISITORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""))
                .allMatch(a -> !a.getSecondName().equals(""))
                .allMatch(a -> !(a.getAge() == 0));
    }

    @DisplayName("загружать список посетителей, привязанных к книге с конкретным id")
    @Test
    void shouldReturnCorrectLinkedVisitorsListByBookId() {
        val authors = jdbc.getVisitorsByBookId(LINKED_BOOK_ID);
        assertThat(authors).isNotNull().hasSize(EXPECTED_LINKED_VISITORS_COUNT)
                .allMatch(a -> !a.getLastName().equals(""))
                .allMatch(a -> !a.getFirstName().equals(""))
                .anyMatch(a -> a.getLastName().equals("Иванов") && a.getFirstName().equals("Иван") &&
                        a.getSecondName().equals("Иванович"))
                .anyMatch(a -> a.getLastName().equals("Петров") && a.getFirstName().equals("Петр") &&
                        a.getSecondName().equals("Петрович"));
    }

    @DisplayName("удалять в БД посетителя по его id (на посетителя не должно быть ссылок в таблице visitor_book)")
    @Test
    void shouldDeleteVisitorById() {
        jdbc.deleteById(EXPECTED_VISITORS_COUNT);
        assertThat(jdbc.existById(EXPECTED_VISITORS_COUNT)).isFalse();
    }

}
