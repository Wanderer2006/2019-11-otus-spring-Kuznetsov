package ru.kusoft.library.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kusoft.library.service.*;

@Component
@RequiredArgsConstructor
public class LibraryApplicationEventListener {

    private final IOService io;
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenreService genreService;
    private final VisitorService visitorService;

    // Обработка событий по книгам (в том числе выдача и возврат книг Посетителям/от Посетителей)
    @EventListener
    public void onShowAllBooksShortEvent(ShowAllBooksShortEvent showAllBooksShortEvent) {
        bookService.showAllBooksShortInfo();
    }

    @EventListener
    public void onShowAllBooksFullEvent(ShowAllBooksFullEvent showAllBooksFullEvent) {
        bookService.showAllBooksFullInfo();
    }

    @EventListener
    public void onDeleteBookEvent(DeleteBookEvent deleteBookEvent) {
        bookService.deleteBookById(deleteBookEvent.getId());
    }

    @EventListener
    public void onGiveOutBookEvent(GiveOutBookEvent giveOutBookEvent) {
        bookService.giveOutBook(giveOutBookEvent.getRelation());
    }

    @EventListener
    public void returnBookEvent(ReturnBookEvent returnBookEvent) {
        bookService.returnBook(returnBookEvent.getRelation());
    }

    @EventListener
    public void addNewBookEvent(AddNewBookEvent addNewBookEvent) {
        bookService.addNewBook();
    }

    // Обработка событий по Авторам
    @EventListener
    public void onShowAuthorsEvent(ShowAuthorsEvent showAuthorsEvent) {
        authorService.showAuthors();
    }

    @EventListener
    public void onDeleteAuthorEvent(DeleteAuthorEvent deleteAuthorEvent) {
        authorService.deleteAuthorById(deleteAuthorEvent.getId());
    }

    @EventListener
    public void onAddNewAuthorEvent(AddNewAuthorEvent addNewAuthorEvent) {
        authorService.addNewAuthor();
    }

    // Обработка событий по Издательствам
    @EventListener
    public void onShowPublishersEvent(ShowPublishersEvent showPublishersEvent) {
        publisherService.showPublishers();
    }

    @EventListener
    public void onDeletePublisherEvent(DeletePublisherEvent deletePublisherEvent) {
        publisherService.deletePublisherById(deletePublisherEvent.getId());
    }

    @EventListener
    public void onAddNewPublisherEvent(AddNewPublisherEvent addNewPublisherEvent) {
        publisherService.addNewPublisher();
    }

    // Обработка событий по Жанрам
    @EventListener
    public void onShowGenresEvent(ShowGenresEvent showGenresEvent) {
        genreService.showGenres();
    }

    @EventListener
    public void onDeleteGenreEvent(DeleteGenreEvent deleteGenreEvent) {
        genreService.deleteGenreById(deleteGenreEvent.getId());
    }

    @EventListener
    public void onAddNewGenreEvent(AddNewGenreEvent addNewGenreEvent) {
        genreService.addNewGenre();
    }

    // Обработка событий по Посетителям
    @EventListener
    public void onShowVisitorsEvent(ShowVisitorsEvent showVisitorsEvent) {
        visitorService.showVisitors();
    }

    @EventListener
    public void onDeleteVisitorEvent(DeleteVisitorEvent deleteVisitorEvent) {
        visitorService.deleteVisitorById(deleteVisitorEvent.getId());
    }

    @EventListener
    public void onAddNewVisitorEvent(AddNewVisitorEvent addNewVisitorEvent) {
        visitorService.addNewVisitor();
    }
}
