package ru.kusoft.library.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Visitor")
class VisitorTest {

    private static final String VISITOR_LAST_NAME = "Посетителев";
    private static final String VISITOR_FIRST_NAME = "Посетитель";
    private static final String VISITOR_SECOND_NAME = "Посетителевич";
    private static final int VISITOR_AGE = 120;

    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Visitor visitor = new Visitor(null, VISITOR_LAST_NAME, VISITOR_FIRST_NAME, VISITOR_SECOND_NAME,
                VISITOR_AGE);

        assertAll("visitor",
                () -> assertEquals(VISITOR_LAST_NAME, visitor.getLastName()),
                () -> assertEquals(VISITOR_FIRST_NAME, visitor.getFirstName()),
                () -> assertEquals(VISITOR_SECOND_NAME, visitor.getSecondName()),
                () -> assertEquals(VISITOR_AGE, visitor.getAge()));
    }

}