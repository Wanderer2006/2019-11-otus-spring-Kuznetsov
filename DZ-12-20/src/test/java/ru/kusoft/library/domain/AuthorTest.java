package ru.kusoft.library.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Author ")
class AuthorTest {

    private static final String AUTHOR_LAST_NAME = "Авторов";
    private static final String AUTHOR_FIRST_NAME = "Автор";
    private static final String AUTHOR_SECOND_NAME = "Авторович";

    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Author author = new Author(null, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME, AUTHOR_SECOND_NAME);

        assertAll("author",
                () -> assertEquals(AUTHOR_LAST_NAME, author.getLastName()),
                () -> assertEquals(AUTHOR_FIRST_NAME, author.getFirstName()),
                () -> assertEquals(AUTHOR_SECOND_NAME, author.getSecondName()));
    }

}