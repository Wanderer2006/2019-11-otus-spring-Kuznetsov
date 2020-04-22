package ru.kusoft.library.domain;

import lombok.Data;

import java.util.List;

@Data
public class Book {
    private final Long bookId;
    private final String title;
    private final int copies;
    private final Publisher publisher;
    private final int yearPublishing;
    private final int printing;
    private final int ageLimit;
    private final List<Author> authors;
    private final List<Genre> genres;
}
