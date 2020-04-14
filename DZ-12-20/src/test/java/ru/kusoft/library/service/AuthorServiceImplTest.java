package ru.kusoft.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kusoft.library.dao.AuthorDao;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы AuthorService должны ")
@SpringBootTest
class AuthorServiceImplTest {

    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_LAST_NAME = "Фамилия";
    private static final String AUTHOR_FIRST_NAME = "Имя";
    private static final String AUTHOR_SECOND_NAME = "Отчество";
    private static final Author AUTHOR = new Author(AUTHOR_ID, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME, AUTHOR_SECOND_NAME);

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private IOService io;

    @MockBean
    private RelationHelper bookAuthorRelation;

/*    @Mock
    private List<Author> authorList;*/

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("вызывать методы authorDao и io с нужными параметрами. Текущий метод: showAuthors")
    void shouldShowAuthors() {
        List<Author> authorList = new ArrayList<Author>() {{
            add(AUTHOR);
            add(AUTHOR);
            add(AUTHOR);
        }};
        given(authorDao.getAll()).willReturn(authorList);
        authorService.showAuthors();
        verify(authorDao, times(1)).getAll();
        verify(io, times(1)).print(anyString(), anyString(), anyString());
        //TODO не победил проверку вызова лямбды в authors.forEach()
/*        given(authorList.get(anyInt())).willReturn(new Author(1L, "Фамилия", "Имя", "Отчество"));
        given(authorList.size()).willReturn(3);
        verify(authorList, times(1)).forEach(author -> {});*/
    }

    @Test
    @DisplayName("вызывать методы authorDao и io с нужными параметрами. Текущий метод: addNewAuthor " +
            "(автор уже существует)")
    void shouldNotInsertExistingAuthor() {
        given(authorDao.existAuthor(any())).willReturn(true);
        given(authorDao.getByAuthor(any())).willReturn(AUTHOR);
        authorService.addNewAuthor();
        verify(io, times(3)).inputStringWithPrompt(anyString());
        verify(authorDao, times(1)).existAuthor(any());
        verify(io, times(1)).println(anyString(), anyString(), anyString(), anyString());
        verify(authorDao, times(0)).insert(any());
    }

    @Test
    @DisplayName("вызывать методы authorDao и io с нужными параметрами. Текущий метод: addNewAuthor " +
            "(автор не существует)")
    void shouldInsertNotExistingAuthor() {
        given(authorDao.existAuthor(any())).willReturn(false);
        given(authorDao.getByAuthor(any())).willReturn(AUTHOR);
        authorService.addNewAuthor();
        verify(io, times(3)).inputStringWithPrompt(anyString());
        verify(authorDao, times(1)).existAuthor(any());
        verify(authorDao, times(1)).insert(any());
        verify(io, times(1)).println(anyString());
    }

    @Test
    @DisplayName("вызывать методы authorDao и io с нужными параметрами. Текущий метод: deleteAuthorById " +
            "(id автора привязан к книге)")
    void shouldNotDeleteLibraryBookAuthorById() {
        given(bookAuthorRelation.countByRightId(anyLong())).willReturn(1L);
        authorService.deleteAuthorById(100L);
        verify(bookAuthorRelation, times(1)).countByRightId(100L);
        verify(io, times(1)).println(anyString());
        verify(authorDao, times(0)).deleteById(100L);
    }

    @Test
    @DisplayName("вызывать методы authorDao и io с нужными параметрами. Текущий метод: deleteAuthorById " +
            "(id автора не привязан к книге)")
    void shouldDeleteUnattachedAuthorById() {
        given(bookAuthorRelation.countByRightId(100L)).willReturn(0L);
        authorService.deleteAuthorById(100L);
        verify(bookAuthorRelation, times(1)).countByRightId(100L);
        verify(io, times(1)).println(anyString());
        verify(authorDao, times(1)).deleteById(100L);
    }

}