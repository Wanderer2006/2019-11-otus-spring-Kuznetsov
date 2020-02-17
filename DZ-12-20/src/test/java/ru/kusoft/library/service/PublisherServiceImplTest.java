package ru.kusoft.library.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kusoft.library.dao.PublisherDao;
import ru.kusoft.library.domain.Publisher;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы PublisherService должны ")
@SpringBootTest
class PublisherServiceImplTest {

    private static final long PUBLISHER_ID = 1L;
    private static final String PUBLISHER_NAME = "Сказители";
    private static final Publisher PUBLISHER = new Publisher(PUBLISHER_ID, PUBLISHER_NAME);

    @MockBean
    private PublisherDao publisherDao;

    @MockBean
    private IOService io;

    @Autowired
    private PublisherService publisherService;

    @Test
    @DisplayName("вызывать методы publisherDao и io с нужными параметрами. Текущий метод: showPublishers")
    void shouldShowPublishers() {
        List<Publisher> publisherList = new ArrayList<Publisher>() {{
            add(PUBLISHER);
            add(PUBLISHER);
            add(PUBLISHER);
        }};
        given(publisherDao.getAll()).willReturn(publisherList);
        publisherService.showPublishers();
        verify(publisherDao, times(1)).getAll();
        verify(io, times(1)).print(anyString(), anyString(), anyString());
        //TODO так же как и в AuthorServiceImplTest не победил проверку вызова лямбды в publishers.forEach()
    }

    @Test
    @DisplayName("вызывать методы publisherDao и io с нужными параметрами. Текущий метод: addNewPublisher " +
            "(издательство уже существует)")
    void shouldNotInsertExistingPublisher() {
        given(io.inputStringWithPrompt(anyString())).willReturn(PUBLISHER_NAME);
        given(publisherDao.existByName(anyString())).willReturn(true);
        given(publisherDao.getByName(anyString())).willReturn(PUBLISHER);
        publisherService.addNewPublisher();
        verify(io, times(1)).inputStringWithPrompt(anyString());
        verify(publisherDao, times(1)).existByName(anyString());
        verify(io, times(1)).println(anyString(), anyString());
        verify(publisherDao, times(0)).insert(any());
    }

    @Test
    @DisplayName("вызывать методы publisherDao и io с нужными параметрами. Текущий метод: addNewPublisher " +
            "(издательство не существует)")
    void shouldInsertNotExistingPublisher() {
        given(io.inputStringWithPrompt(anyString())).willReturn(PUBLISHER_NAME);
        given(publisherDao.existByName(anyString())).willReturn(false);
        given(publisherDao.getByName(anyString())).willReturn(PUBLISHER);
        publisherService.addNewPublisher();
        verify(io, times(1)).inputStringWithPrompt(anyString());
        verify(publisherDao, times(1)).existByName(anyString());
        verify(publisherDao, times(1)).insert(any());
        verify(io, times(1)).println(anyString());
    }

    @Test
    @DisplayName("вызывать методы publisherDao и io с нужными параметрами. Текущий метод: deletePublisherById " +
            "(id издательства привязан к книге)")
    void shouldNotDeleteLibraryBookPublisherById() {
        given(publisherDao.existRelationById(anyLong())).willReturn(true);
        publisherService.deletePublisherById(100L);
        verify(publisherDao, times(1)).existRelationById(100L);
        verify(io, times(1)).println(anyString());
        verify(publisherDao, times(0)).deleteById(100L);
    }

    @Test
    @DisplayName("вызывать методы publisherDao и io с нужными параметрами. Текущий метод: deletePublisherById " +
            "(id издательства не привязан к книге)")
    void shouldDeleteUnattachedPublisherById() {
        given(publisherDao.existRelationById(anyLong())).willReturn(false);
        publisherService.deletePublisherById(100L);
        verify(publisherDao, times(1)).existRelationById(100L);
        verify(io, times(1)).println(anyString());
        verify(publisherDao, times(1)).deleteById(100L);
    }
}