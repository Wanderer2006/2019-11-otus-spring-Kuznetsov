package ru.kusoft.library.service;

import ru.kusoft.library.domain.Author;

import java.util.List;

public interface AuthorService {
    void showAuthors();

    void deleteAuthorById(Long id);

    void addNewAuthor();

    List<Author> getAuthors();
}
