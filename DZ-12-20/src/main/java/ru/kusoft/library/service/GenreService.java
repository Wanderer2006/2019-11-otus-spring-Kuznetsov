package ru.kusoft.library.service;

import ru.kusoft.library.domain.Genre;

import java.util.List;

public interface GenreService {

    void showGenres();

    void deleteGenreById(Long id);

    void addNewGenre();

    List<Genre> getGenres();
}
