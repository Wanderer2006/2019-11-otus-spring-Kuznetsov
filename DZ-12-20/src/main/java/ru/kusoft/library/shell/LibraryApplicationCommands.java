package ru.kusoft.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.kusoft.library.dao.ext.Relation;
import ru.kusoft.library.service.*;

@ShellComponent
@RequiredArgsConstructor
public class LibraryApplicationCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenreService genreService;
    private final VisitorService visitorService;
    private final IOService io;

    // Команды по книгам (в том числе выдача и возврат книг Посетителям/от Посетителей)
    @ShellMethod(value = "Вывести сокращенный список книг", key = {"ss", "show-all-books-short"})
    public void showAllBooksShort() {
        bookService.showAllBooksShortInfo();
    }

    @ShellMethod(value = "Вывести полный список книг", key = {"sf", "show-all-books-full"})
    public void showAllBooksFull() {
        bookService.showAllBooksFullInfo();
    }

    @ShellMethod(value = "Удалить книгу по ее номеру (Пример: db <номер книги>)", key = {"db", "delete-books"})
    public void deleteBook(@ShellOption(defaultValue = "not value") String bookIdStr) {
        if ("not value".equals(bookIdStr) || !bookIdStr.matches("^[0-9]+$")) {
            io.print("Номер книги должен содержать только цифры");
        } else {
            Long bookId = Long.valueOf(bookIdStr);
            bookService.deleteBookById(bookId);
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
            bookService.giveOutBook(new Relation(visitorId, bookId));
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
            bookService.returnBook(new Relation(visitorId, bookId));        }
    }

    @ShellMethod(value = "Добавить (получить/подарить) новую книгу в библиотеку", key = {"ab", "add-book"})
    public void addNewBook() {
        bookService.addNewBook();
    }

    // Команды по авторам
    @ShellMethod(value = "Вывести список авторов", key = {"sa", "show-authors"})
    public void showAuthors() {
        authorService.showAuthors();
    }

    @ShellMethod(value = "Добавить автора", key = {"aa", "add-author"})
    public void addNewAuthor() {
        authorService.addNewAuthor();
    }

    @ShellMethod(value = "Удалить автора по его номеру (Пример: da <номер автора>)", key = {"da", "delete-author"})
    public void deleteAuthor(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер автора должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            authorService.deleteAuthorById(id);
        }
    }

    // Команды по издательствам
    @ShellMethod(value = "Вывести список Издательств", key = {"sp", "show-publishers"})
    public void showPublishers() {
        publisherService.showPublishers();
    }

    @ShellMethod(value = "Добавить издательство", key = {"ap", "add-publisher"})
    public void addNewPublisher() {
        publisherService.addNewPublisher();
    }

    @ShellMethod(value = "Удалить Издательство по его номеру (Пример: dp <номер издательства>)",
            key = {"dp", "delete-publisher"})
    public void deletePublisher(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер Издательства должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            publisherService.deletePublisherById(id);
        }
    }

    // Команды по жанрам
    @ShellMethod(value = "Вывести список жанров", key = {"sg", "show-genres"})
    public void showGenres() {
        genreService.showGenres();
    }

    @ShellMethod(value = "Добавить жанр", key = {"ag", "add-genre"})
    public void addNewGenre() {
        genreService.addNewGenre();
    }

    @ShellMethod(value = "Удалить жанр по его номеру (Пример: dg <номер жанра>)", key = {"dg", "delete-genre"})
    public void deleteGenre(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            System.out.println("Номер жанра должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            genreService.deleteGenreById(id);
        }
    }

    // Команды по посетителям
    @ShellMethod(value = "Вывести список посетителей", key = {"sv", "show-visitors"})
    public void showVisitors() {
        visitorService.showVisitors();
    }

    @ShellMethod(value = "Добавить посетителя", key = {"av", "add-visitor"})
    public void addNewVisitor() {
        visitorService.addNewVisitor();
    }

    @ShellMethod(value = "Удалить посетителя по его номеру (Пример: dv <номер посетителя>)",
            key = {"dv", "delete-visitor"})
    public void deleteVisitor(@ShellOption(defaultValue = "not value") String idStr) {
        if ("not value".equals(idStr) || !idStr.matches("^[0-9]+$")) {
            io.print("Номер посетителя должен содержать только цифры");
        } else {
            Long id = Long.valueOf(idStr);
            visitorService.deleteVisitorById(id);
        }
    }

}
