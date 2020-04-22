package ru.kusoft.library.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Genre ")
class GenreTest {

    private static final String GENRE = "Народное творчество";

    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Genre genre = new Genre(null, GENRE);

        assertAll("genre",
                () -> assertEquals(GENRE, genre.getGenre()));
    }

}