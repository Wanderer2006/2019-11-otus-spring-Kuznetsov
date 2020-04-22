package ru.kusoft.library.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Publisher ")
class PublisherTest {

    private static final String PUBLISHER = "Народные сказители";

    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Publisher publisher = new Publisher(null, PUBLISHER);

        assertAll("publisher",
                () -> assertEquals(PUBLISHER, publisher.getPublisherName()));
    }
}