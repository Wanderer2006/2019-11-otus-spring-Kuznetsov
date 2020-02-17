package ru.kusoft.library.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Book")
class BookTest {

    private static final String TITLE = "Русские народные сказки";
    private static final int COPIES = 10;
    private static final String PUBLISHER_NAME = "Сказители";
    private static final int YEAR_PUBLISHING = 1500;
    private static final int PRINTING = 100500;
    private static final int AGE_LIMIT = 1;
    private static final String AUTHOR_LAST_NAME = "Народов";
    private static final String AUTHOR_FIRST_NAME = "Народ";
    private static final String AUTHOR_SECOND_NAME = "Народович";
    private static final String GENRE = "Народное творчество";

    @DisplayName("корректно создаётся конструктором")
    @Test
    void shouldHaveCorrectConstructor() {
        Book book = new Book(null, TITLE, COPIES,
                new Publisher(null, PUBLISHER_NAME), YEAR_PUBLISHING, PRINTING, AGE_LIMIT,
                new ArrayList<Author>() {{
                    add(new Author(null, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME, AUTHOR_SECOND_NAME));
                }},
                new ArrayList<Genre>() {{
                    add(new Genre(null, GENRE));
                }});

        assertAll("book",
                () -> assertEquals(TITLE, book.getTitle()),
                () -> assertEquals(COPIES, book.getCopies()),
                () -> assertEquals(PUBLISHER_NAME, book.getPublisher().getPublisherName()),
                () -> assertEquals(YEAR_PUBLISHING, book.getYearPublishing()),
                () -> assertEquals(PRINTING, book.getPrinting()),
                () -> assertEquals(AGE_LIMIT, book.getAgeLimit()),
                () -> assertEquals(AUTHOR_LAST_NAME, book.getAuthors().get(0).getLastName()),
                () -> assertEquals(AUTHOR_FIRST_NAME, book.getAuthors().get(0).getFirstName()),
                () -> assertEquals(AUTHOR_SECOND_NAME, book.getAuthors().get(0).getSecondName()),
                () -> assertEquals(GENRE, book.getGenres().get(0).getGenre()));
    }

}