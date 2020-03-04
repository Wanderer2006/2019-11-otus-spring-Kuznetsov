package ru.kusoft.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kusoft.library.dao.VisitorDao;
import ru.kusoft.library.domain.Visitor;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы VisitorService должны ")
@SpringBootTest
class VisitorServiceImplTest {

    private static final long VISITOR_ID = 1L;
    private static final String VISITOR_LAST_NAME = "Фамилия";
    private static final String VISITOR_FIRST_NAME = "Имя";
    private static final String VISITOR_SECOND_NAME = "Отчество";
    private static final int VISITOR_AGE = 12;
    private static final Visitor VISITOR = new Visitor(VISITOR_ID, VISITOR_LAST_NAME, VISITOR_FIRST_NAME,
            VISITOR_SECOND_NAME, VISITOR_AGE);

    @MockBean
    private VisitorDao visitorDao;

    @MockBean
    private IOService io;

    @Autowired
    private VisitorService visitorService;

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: showVisitors")
    void shouldShowVisitors() {
        List<Visitor> visitorList = new ArrayList<Visitor>() {{
            add(VISITOR);
            add(VISITOR);
            add(VISITOR);
        }};
        given(visitorDao.getAll()).willReturn(visitorList);
        visitorService.showVisitors();
        verify(visitorDao, times(1)).getAll();
        verify(io, times(1)).print(anyString(), anyString(), anyString());
        //TODO так же как и в AuthorServiceImplTest не победил проверку вызова лямбды в visitors.forEach()
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: addNewVisitor " +
            "(посетитель уже существует)")
    void shouldNotInsertExistingVisitor() {
        given(visitorDao.existVisitor(any())).willReturn(true);
        given(visitorDao.getByVisitor(any())).willReturn(VISITOR);
        visitorService.addNewVisitor();
        verify(io, times(3)).inputStringWithPrompt(anyString());
        verify(io, times(1)).inputIntWithPrompt(anyString());
        verify(visitorDao, times(1)).existVisitor(any());
        verify(io, times(1)).println(anyString(), anyString(), anyString(), anyString(), anyInt());
        verify(visitorDao, times(0)).insert(any());
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: addNewVisitor " +
            "(посетитель не существует)")
    void shouldInsertNotExistingVisitor() {
        given(visitorDao.existVisitor(any())).willReturn(false);
        given(visitorDao.getByVisitor(any())).willReturn(VISITOR);
        visitorService.addNewVisitor();
        verify(io, times(3)).inputStringWithPrompt(anyString());
        verify(io, times(1)).inputIntWithPrompt(anyString());
        verify(visitorDao, times(1)).existVisitor(any());
        verify(visitorDao, times(1)).insert(any());
        verify(io, times(1)).println(anyString());
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: deleteVisitorById " +
            "(id посетителя привязан к книге)")
    void shouldNotDeleteLibraryBookVisitorById() {
        given(visitorDao.existBookAtVisitor(anyLong())).willReturn(true);
        visitorService.deleteVisitorById(100L);
        verify(visitorDao, times(1)).existBookAtVisitor(100L);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(0)).deleteById(100L);
    }

    @Test
    @DisplayName("вызывать методы visitorDao и io с нужными параметрами. Текущий метод: deleteVisitorById " +
            "(id посетителя не привязан к книге)")
    void shouldDeleteUnattachedVisitorById() {
        given(visitorDao.existBookAtVisitor(anyLong())).willReturn(false);
        visitorService.deleteVisitorById(100L);
        verify(visitorDao, times(1)).existBookAtVisitor(100L);
        verify(io, times(1)).println(anyString());
        verify(visitorDao, times(1)).deleteById(100L);
    }

}