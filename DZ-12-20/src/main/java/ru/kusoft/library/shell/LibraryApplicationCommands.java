package ru.kusoft.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.kusoft.library.dao.ext.Relation;
import ru.kusoft.library.events.*;
import ru.kusoft.library.service.IOService;

@ShellComponent
@RequiredArgsConstructor
public class LibraryApplicationCommands {

    private final ApplicationEventPublisher publisher;
    private final IOService io;

    // Команды по книгам (в том числе выдача и возврат книг Посетителям/от Посетителей)
    @ShellMethod(value = "Вывести сокращенный список книг", key = {"ss", "show-all-books-short"})
    public void showAllBooksShort() {
        publisher.publishEvent(new ShowAllBooksShortEvent(this));
    }

    @ShellMethod(value = "Вывести полный список книг", key = {"sf", "show-all-books-full"})
    public void showAllBooksFull() {
        publisher.publishEvent(new ShowAllBooksFullEvent(this));
    }

    @ShellMethod(value = "Удалить книгу по ее номеру (Пример: db <номер книги>)", key = {"db", "delete-books"})
    public void deleteBook(@ShellOption(defaultValue = "not value") String bookIdStr) {
        if ("not value".equals(bookIdStr) || !bookIdStr.matches("^[0-9]+$")) {
            io.print("Номер книги должен содержать только цифры");
        } else {
            Long bookId = Long.valueOf(bookIdStr);
            publisher.publishEvent(new DeleteBookEvent(this, bookId));
        }
    }

    @ShellMethod(value = "Выдать книгу посетителю по ее номеру и номеру посетителя " +
            "(Пример: gob <номер книги> <номер посетителя>)", key = {"gob", "give-out-book"})
    public void giveOutBook(@ShellOption(defaultValue = "not value") String bookIdStr,
                            @ShellOption(defaultValue = "not value") String visitorIdStr) {
        if ("not value".equals(bookIdStr) || !bookIdStr.matches("^[0-9]+$")) {
            io.print("Номер книги должен содержать только цифры");
        } else if ("not value".equals(visitorIdStr) || !visitorIdStr.matches("^[0-9]+$")) {
            io.print("Номер посетителя должен содержать только цифры");
        } else {
            Long bookId = Long.valueOf(bookIdStr);
            Long visitorId = Long.valueOf(visitorIdStr);
            publisher.publishEvent(new GiveOutBookEvent(this, new Relation(visitorId, bookId)));
        }
    }

    @ShellMethod(value = "Принять книгу от посетителя по ее номеру и номеру посетителя " +
            "(Пример: rb <номер книги> <номер посетителя>)", key = {"rb", "return-book"})
    public void returnBook(@ShellOption(defaultValue = "not value") String bookIdStr,
                           @ShellOption(defaultValue = "not value") String visitorIdStr) {
        if ("not value".equals(bookIdStr) || !bookIdStr.matches("^[0-9]+$")) {
            io.print("Номер книги должен содержать только цифры");
        } else if ("not value".equals(visitorIdStr) || !visitorIdStr.matches("^[0-9]+$")) {
            io.print("Номер посетителя должен содержать только цифры");
        } else {
            Long bookId = Long.valueOf(bookIdStr);
            Long visitorId = Long.valueOf(visitorIdStr);
            publisher.publishEvent(new ReturnBookEvent(this, new Relation(visitorId, bookId)));
        }
    }

    @ShellMethod(value = "Добавить (получить/подарить) новую книгу в библиотеку", key = {"ab", "add-book"})
    public void addNewBook() {
        publisher.publishEvent(new AddNewBookEvent(this));
    }

    // Команды по авторам
    @ShellMethod(value = "Вывести список авторов", key = {"sa", "show-authors"})
    public void showAuthors() {
        publisher.publishEvent(new ShowAuthorsEvent(this));
    }

    @ShellMethod(value = "Добавить автора", key = {"aa", "add-author"})
    public void addNewAuthor() {
        publisher.publishEvent(new AddNewAuthorEvent(this));
    }

    @ShellMethod(value = "Удалить автора по его номеру (Пример: da <номер автора>)", key = {"da", "delete-author"})
    public void deleteAuthor(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер автора должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            publisher.publishEvent(new DeleteAuthorEvent(this, id));
        }
    }

    // Команды по издательствам
    @ShellMethod(value = "Вывести список Издательств", key = {"sp", "show-publishers"})
    public void showPublishers() {
        publisher.publishEvent(new ShowPublishersEvent(this));
    }

    @ShellMethod(value = "Добавить издательство", key = {"ap", "add-publisher"})
    public void addNewPublisher() {
        publisher.publishEvent(new AddNewPublisherEvent(this));
    }

    @ShellMethod(value = "Удалить Издательство по его номеру (Пример: dp <номер издательства>)",
            key = {"dp", "delete-publisher"})
    public void deletePublisher(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер Издательства должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            publisher.publishEvent(new DeletePublisherEvent(this, id));
        }
    }

    // Команды по жанрам
    @ShellMethod(value = "Вывести список жанров", key = {"sg", "show-genres"})
    public void showGenres() {
        publisher.publishEvent(new ShowGenresEvent(this));
    }

    @ShellMethod(value = "Добавить жанр", key = {"ag", "add-genre"})
    public void addNewGenre() {
        publisher.publishEvent(new AddNewGenreEvent(this));
    }

    @ShellMethod(value = "Удалить жанр по его номеру (Пример: dg <номер жанра>)", key = {"dg", "delete-genre"})
    public void deleteGenre(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            System.out.println("Номер жанра должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            publisher.publishEvent(new DeleteGenreEvent(this, id));
        }
    }

    // Команды по посетителям
    @ShellMethod(value = "Вывести список посетителей", key = {"sv", "show-visitors"})
    public void showVisitors() {
        publisher.publishEvent(new ShowVisitorsEvent(this));
    }

    @ShellMethod(value = "Добавить посетителя", key = {"av", "add-visitor"})
    public void addNewVisitor() {
        publisher.publishEvent(new AddNewVisitorEvent(this));
    }

    @ShellMethod(value = "Удалить посетителя по его номеру (Пример: dv <номер посетителя>)",
            key = {"dv", "delete-visitor"})
    public void deleteVisitor(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер посетителя должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            publisher.publishEvent(new DeleteVisitorEvent(this, id));
        }
    }

}
