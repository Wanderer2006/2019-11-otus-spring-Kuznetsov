package ru.kusoft.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kusoft.library.dao.BookDao;
import ru.kusoft.library.dao.VisitorDao;
import ru.kusoft.library.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы AuthorService должны ")
@SpringBootTest
class BookServiceImplTest {

    private static final long BOOK_ID = 1L;
    private static final String TITLE = "Русские народные сказки";
    private static final int COPIES = 10;
    private static final long PUBLISHER_ID = 1L;
    private static final String PUBLISHER_NAME = "Сказители";
    private static final int YEAR_PUBLISHING = 1500;
    private static final int PRINTING = 100500;
    private static final int AGE_LIMIT_MATCH = 1;
    private static final int AGE_LIMIT_NOT_MATCH = 13;
    private static final long AUTHOR_ID = 1L;
    private static final String AUTHOR_LAST_NAME = "Народов";
    private static final String AUTHOR_FIRST_NAME = "Народ";
    private static final String AUTHOR_SECOND_NAME = "Народович";
    private static final long GENRE_ID = 1L;
    private static final String GENRE = "Народное творчество";

    private static final Publisher PUBLISHER = new Publisher(PUBLISHER_ID, PUBLISHER_NAME);
    private static final List<Author> AUTHOR_LIST = new ArrayList<Author>() {{
        add(new Author(AUTHOR_ID, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME, AUTHOR_SECOND_NAME));
    }};
    private static final List<Genre> GENRE_LIST = new ArrayList<Genre>() {{
        add(new Genre(GENRE_ID, GENRE));
    }};

    private static final Book BOOK_MATCH_AGE = new Book(BOOK_ID, TITLE, COPIES, PUBLISHER, YEAR_PUBLISHING,
            PRINTING, AGE_LIMIT_MATCH, AUTHOR_LIST, GENRE_LIST);
    private static final Book BOOK_NOT_MATCH_AGE = new Book(BOOK_ID, TITLE, COPIES, PUBLISHER, YEAR_PUBLISHING,
            PRINTING, AGE_LIMIT_NOT_MATCH, AUTHOR_LIST, GENRE_LIST);

    private static final long NOT_ISSUED_VISITOR_ID = 1L;
    private static final long ISSUED_VISITOR_ID = 2L;
    private static final String VISITOR_LAST_NAME = "Посетителев";
    private static final String VISITOR_FIRST_NAME = "Посетитель";
    private static final String VISITOR_SECOND_NAME = "Посетителевич";
    private static final int VISITOR_AGE = 12;
    private static final Visitor VISITOR = new Visitor(ISSUED_VISITOR_ID, VISITOR_LAST_NAME, VISITOR_FIRST_NAME,
            VISITOR_SECOND_NAME, VISITOR_AGE);

    private static final Relation ISSUED_RELATION = new Relation(ISSUED_VISITOR_ID, BOOK_ID);
    private static final Relation NOT_ISSUED_RELATION = new Relation(NOT_ISSUED_VISITOR_ID, BOOK_ID);

    @MockBean
    private BookDao bookDao;

    @MockBean
    private VisitorDao visitorDao;

    @MockBean
    private IOService io;

    @MockBean
    private PublisherService publisherService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("вызывать методы bookDao и io с нужными параметрами. Текущий метод: showAllBooksShortInfo")
    void shouldShowAllBooksShortInfo() {
        List<Book> bookList = new ArrayList<Book>() {{
            add(BOOK_MATCH_AGE);
            add(BOOK_MATCH_AGE);
            add(BOOK_MATCH_AGE);
        }};
        given(bookDao.getAllShortInfo()).willReturn(bookList);
        bookService.showAllBooksShortInfo();
        verify(bookDao, times(1)).getAllShortInfo();
        verify(io, times(1)).print(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString());
        //TODO так же как и в AuthorServiceImplTest не победил проверку вызова лямбды в books.forEach()
    }

    @Test
    @DisplayName("вызывать методы bookDao, visitorDao и io с нужными параметрами. Текущий метод: showAllBooksFullInfo")
    void shouldShowAllBooksFullInfo() {
        List<Book> bookList = new ArrayList<Book>() {{
            add(BOOK_MATCH_AGE);
            add(BOOK_MATCH_AGE);
            add(BOOK_MATCH_AGE);
        }};
        List<Visitor> visitorList = new ArrayList<Visitor>() {{
            add(VISITOR);
        }};
        List<Relation> relationList = new ArrayList<Relation>() {{
            add(ISSUED_RELATION);
        }};
        given(bookDao.getAllFullInfo()).willReturn(bookList);
        given(visitorDao.getAllUsed()).willReturn(visitorList);
        given(visitorDao.getAllRelations()).willReturn(relationList);
        bookService.showAllBooksFullInfo();
        verify(bookDao, times(1)).getAllFullInfo();
        verify(io, times(1)).print(anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        //TODO так же как и в AuthorServiceImplTest не победил проверку вызова лямбды в books.forEach()
    }

    @Test
    @DisplayName("вызывать методы bookDao, visitorDao и io с нужными параметрами. Текущий метод: giveOutBook " +
            "(все экземпляры на руках)")
    void shouldNotGiveOutBookAllIssued() {
        given(bookDao.getById(BOOK_ID)).willReturn(BOOK_MATCH_AGE);
        given(visitorDao.countRelationByBookId(BOOK_ID)).willReturn(Long.valueOf(COPIES));
        bookService.giveOutBook(ISSUED_RELATION);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(0)).insertRelation(BOOK_ID, ISSUED_VISITOR_ID);
    }

    @Test
    @DisplayName("вызывать методы bookDao, visitorDao и io с нужными параметрами. Текущий метод: giveOutBook " +
            "(возраст не соответствует)")
    void shouldNotGiveOutBookNotMatchAge() {
        given(bookDao.getById(BOOK_ID)).willReturn(BOOK_NOT_MATCH_AGE);
        given(visitorDao.countRelationByBookId(BOOK_ID)).willReturn(Long.valueOf(COPIES - 1));
        given(visitorDao.getById(ISSUED_VISITOR_ID)).willReturn(VISITOR);
        bookService.giveOutBook(ISSUED_RELATION);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(0)).insertRelation(BOOK_ID, ISSUED_VISITOR_ID);
    }

    @Test
    @DisplayName("вызывать методы bookDao, visitorDao и io с нужными параметрами. Текущий метод: giveOutBook " +
            "(все условия соблюдены)")
    void shouldGiveOutBook() {
        given(bookDao.getById(BOOK_ID)).willReturn(BOOK_MATCH_AGE);
        given(visitorDao.countRelationByBookId(BOOK_ID)).willReturn(Long.valueOf(COPIES - 1));
        given(visitorDao.getById(ISSUED_VISITOR_ID)).willReturn(VISITOR);
        bookService.giveOutBook(ISSUED_RELATION);
        verify(visitorDao, times(1)).insertRelation(BOOK_ID, ISSUED_VISITOR_ID);
        verify(io, times(1)).println(anyString());
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: returnBook " +
            "(все экземпляры в библиотеке)")
    void shouldNotReturnBookNotIssued() {
        given(visitorDao.existRelationByBookId(anyLong())).willReturn(false);
        bookService.returnBook(ISSUED_RELATION);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(0)).deleteRelation(BOOK_ID, ISSUED_VISITOR_ID);
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: returnBook " +
            "(книга не выдавалась посетителю)")
    void shouldNotReturnBookNotIssuedVisitor() {
        List<Visitor> visitorList = new ArrayList<Visitor>() {{
            add(VISITOR);
        }};
        given(visitorDao.existRelationByBookId(anyLong())).willReturn(true);
        given(visitorDao.getVisitorsByBookId(anyLong())).willReturn(visitorList);
        bookService.returnBook(NOT_ISSUED_RELATION);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(0)).deleteRelation(BOOK_ID, NOT_ISSUED_VISITOR_ID);
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: returnBook " +
            "(книга на руках у посетителя)")
    void shouldReturnBook() {
        List<Visitor> visitorList = new ArrayList<Visitor>() {{
            add(VISITOR);
        }};
        given(visitorDao.existRelationByBookId(anyLong())).willReturn(true);
        given(visitorDao.getVisitorsByBookId(anyLong())).willReturn(visitorList);
        bookService.returnBook(ISSUED_RELATION);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(1)).deleteRelation(BOOK_ID, ISSUED_VISITOR_ID);
    }

    @Test
    @DisplayName("вызывать методы bookDao и io с нужными параметрами. Текущий метод: addNewBook")
    void shouldAddNewBook() {
        given(publisherService.getPublisher()).willReturn(PUBLISHER);
        given(authorService.getAuthors()).willReturn(AUTHOR_LIST);
        given(genreService.getGenres()).willReturn(GENRE_LIST);
        bookService.addNewBook();
        verify(bookDao, times(1)).insert(any());
        verify(io, times(1)).println(anyString());
    }

}