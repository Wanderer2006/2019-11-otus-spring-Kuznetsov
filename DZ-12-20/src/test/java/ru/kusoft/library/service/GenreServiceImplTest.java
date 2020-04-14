package ru.kusoft.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kusoft.library.dao.GenreDao;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы GenreService должны ")
@SpringBootTest
class GenreServiceImplTest {

    private static final long GENRE_ID = 1L;
    private static final String GENRE_NAME = "Народное творчество";
    private static final Genre GENRE = new Genre(GENRE_ID, GENRE_NAME);

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private IOService io;

    @MockBean
    private RelationHelper bookGenreRelation;

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("вызывать методы genreDao и io с нужными параметрами. Текущий метод: showGenres")
    void shouldShowGenres() {
        List<Genre> genreList = new ArrayList<Genre>() {{
            add(GENRE);
            add(GENRE);
            add(GENRE);
        }};
        given(genreDao.getAll()).willReturn(genreList);
        genreService.showGenres();
        verify(genreDao, times(1)).getAll();
        verify(io, times(1)).print(anyString(), anyString(), anyString());
        //TODO так же как и в AuthorServiceImplTest не победил проверку вызова лямбды в genres.forEach()
    }

    @Test
    @DisplayName("вызывать методы genreDao и io с нужными параметрами. Текущий метод: addNewGenre (жанр уже существует)")
    void shouldNotInsertExistingGenre() {
        given(io.inputStringWithPrompt(anyString())).willReturn(GENRE_NAME);
        given(genreDao.existByName(anyString())).willReturn(true);
        given(genreDao.getByName(anyString())).willReturn(GENRE);
        genreService.addNewGenre();
        verify(io, times(1)).inputStringWithPrompt(anyString());
        verify(genreDao, times(1)).existByName(anyString());
        verify(io, times(1)).println(anyString(), anyString());
        verify(genreDao, times(0)).insert(any());
    }

    @Test
    @DisplayName("вызывать методы genreDao и io с нужными параметрами. Текущий метод: addNewGenre (жанр не существует)")
    void shouldInsertNotExistingGenre() {
        given(io.inputStringWithPrompt(anyString())).willReturn(GENRE_NAME);
        given(genreDao.existByName(anyString())).willReturn(false);
        given(genreDao.getByName(anyString())).willReturn(GENRE);
        genreService.addNewGenre();
        verify(io, times(1)).inputStringWithPrompt(anyString());
        verify(genreDao, times(1)).existByName(anyString());
        verify(io, times(1)).println(anyString());
        verify(genreDao, times(1)).insert(any());
    }

    @Test
    @DisplayName("вызывать методы genreDao и io с нужными параметрами. Текущий метод: deleteGenreById " +
            "(id жанра привязан к книге)")
    void shouldNotDeleteLibraryBookGenreById() {
        given(bookGenreRelation.countByRightId(anyLong())).willReturn(1L);
        genreService.deleteGenreById(100L);
        verify(bookGenreRelation, times(1)).countByRightId(100L);
        verify(io, times(1)).println(anyString());
        verify(genreDao, times(0)).deleteById(100L);
    }

    @Test
    @DisplayName("вызывать методы genreDao и io с нужными параметрами. Текущий метод: deleteGenreById " +
            "(id жанра не привязан к книге)")
    void shouldDeleteUnattachedGenreById() {
        given(bookGenreRelation.countByRightId(anyLong())).willReturn(0L);
        genreService.deleteGenreById(100L);
        verify(bookGenreRelation, times(1)).countByRightId(100L);
        verify(genreDao, times(1)).deleteById(100L);
        verify(io, times(1)).println(anyString());
    }
}