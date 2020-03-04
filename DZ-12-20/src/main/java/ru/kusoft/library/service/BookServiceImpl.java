package ru.kusoft.library.service;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.kusoft.library.dao.*;
import ru.kusoft.library.dao.ext.Relation;
import ru.kusoft.library.domain.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Service
public class BookServiceImpl implements BookService {
    private static final String FORMAT_BOOK_SHORT_LIST_HEADER = "%5s|%-80.80s|%6s|%-15.15s|%4s|%7s|%7s%n";
    private static final String FORMAT_BOOK_SHORT_LIST_RECORD = "%5d|%-80.80s|%6d|%-15.15s|%4d|%7d|%7s%n";
    private static final String FORMAT_BOOK_FULL_LIST_HEADER = "%5s|%-40.40s|%-80.80s|%-50.50s|%6s|%-15.15s|%4s|%7s|%7s|%-50.50s|%n";
    private static final String FORMAT_BOOK_FULL_LIST_RECORD = "%5d|%-40.40s|%-80.80s|%-50.50s|%6d|%-15.15s|%4d|%7d|%7s|%-50.50s|%n";

    private final BookDao bookDao;
    private final VisitorDao visitorDao;
    private final IOService io;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public void showAllBooksShortInfo() {
        List<Book> books = bookDao.getAllShortInfo();
        io.print(FORMAT_BOOK_SHORT_LIST_HEADER, "Номер", "Наименование", "Экз-ов", "Издательство", "Год",
                "Тираж", "Возраст");
        books.forEach(b -> {
            String publisherName = (b.getPublisher() != null && b.getPublisher().getPublisherName() != null)
                    ? b.getPublisher().getPublisherName() : "";
            io.print(FORMAT_BOOK_SHORT_LIST_RECORD, b.getBookId(), b.getTitle(), b.getCopies(), publisherName,
                    b.getYearPublishing(), b.getPrinting(), b.getAgeLimit() + "+");
        });
    }

    @Override
    public void showAllBooksFullInfo() {
        List<Book> books = bookDao.getAllFullInfo();
        Map<Long, Visitor> visitorsMap = visitorDao.getAllUsed().stream()
                .collect(Collectors.toMap(Visitor::getVisitorId, v -> v));
        List<Relation> visitorRelation = visitorDao.getAllRelations();
        io.print(FORMAT_BOOK_FULL_LIST_HEADER, "Номер", "Авторы", "Наименование", "Жанры", "Экз-ов",
                "Издательство", "Год", "Тираж", "Возраст", "На руках");
        books.forEach(b -> {
            String authorsStr = b.getAuthors().stream()
                    .map(a -> a.getSecondName() != null
                            ? a.getLastName() + " " + a.getFirstName() + " " + a.getSecondName() + "[" + a.getAuthorId() + "]"
                            : a.getFirstName() + " " + a.getLastName() + "[" + a.getAuthorId() + "]")
                    .collect(Collectors.joining(", "));
            String genresStr = b.getGenres().stream()
                    .map(g -> g.getGenre() + "[" + g.getGenreId() + "]")
                    .collect(Collectors.joining(", "));
            String visitorsStr = visitorRelation.stream()
                    .filter(r -> r.getRightId() == b.getBookId())
                    .map(r -> visitorsMap.get(r.getLeftId()))
                    .map(v -> v.getLastName() + " " + v.getFirstName() + " " + v.getSecondName() + "[" + v.getVisitorId() + "]")
                    .collect(Collectors.joining(", "));
            String publisherName = (b.getPublisher() != null && b.getPublisher().getPublisherName() != null)
                    ? b.getPublisher().getPublisherName() : "";
            io.print(FORMAT_BOOK_FULL_LIST_RECORD, b.getBookId(), authorsStr, b.getTitle(), genresStr,
                    b.getCopies(), publisherName, b.getYearPublishing(), b.getPrinting(), b.getAgeLimit() + "+", visitorsStr);
        });
    }

    @Override
    public void deleteBookById(Long id) {
        if (!bookDao.existById(id)) {
            io.println("Номер книги не найден");
        } else if (visitorDao.existVisitorWithBook(id)) {
            io.println("Книгу удалить нельзя, она находится на руках у Посетителей");
        } else {
            try {
                bookDao.deleteById(id);
                io.println("Книга удалена");
            } catch (DataAccessException e) {
                io.println("Не удалось удалить книгу");
            }
        }
    }

    @Override
    public void giveOutBook(Relation relation) {
        Book book = bookDao.getById(relation.getRightId());
        if (book.getCopies() == visitorDao.countVisitorWithBook(relation.getRightId())) {
            io.println("Выдать книгу нельзя. Все доступные экземпляры на руках у Посетителей.");
        } else {
            Visitor visitor = visitorDao.getById(relation.getLeftId());
            if (visitor.getAge() < book.getAgeLimit()) {
                io.println("Выдать книгу нельзя. Возраст посетителя меньше доступного для данной книги.");
            } else {
                try {
                    visitorDao.addBookForVisitor(book.getBookId(), visitor.getVisitorId());
                    io.println("Книга выдана Посетителю.");
                } catch (DataAccessException e) {
                    io.println("Не удалось выдать книгу");
                }
            }
        }
    }

    @Override
    public void returnBook(Relation relation) {
        long bookId = relation.getRightId();
        if (!visitorDao.existVisitorWithBook(bookId)) {
            io.println("Все доступные экземпляры данной книги находятся в библиотеке. " +
                    "Если вы дарите книгу библиотеке, то воспользуйтесь командой add-book");
        } else {
            List<Visitor> visitors = visitorDao.getVisitorsByBookId(bookId);
            Visitor visitor = visitors.stream()
                    .filter(v -> v.getVisitorId() == relation.getLeftId())
                    .findFirst()
                    .orElse(null);
            if (visitor == null) {
                io.println("Данному посетителю эта книга ранее не выдавалась.");
            } else {
                try {
                    visitorDao.deleteBookForVisitor(bookId, visitor.getVisitorId());
                    io.println("Книга возвращена в библиотеку.");
                } catch (DataAccessException e) {
                    io.println("Не удалось вернуть книгу");
                }
            }
        }
    }

    @Override
    public void addNewBook() {
        String title = io.inputStringWithPrompt("Введите наименование книги: ");
        int copies = io.inputIntWithPrompt("Введите количество копий книги: ");
        Publisher publisher = publisherService.getPublisher();
        int yearPublishing = io.inputIntWithPrompt("Введите год издания: ");
        int printing = io.inputIntWithPrompt("Введите тираж издания: ");
        int ageLimit = io.inputIntWithPrompt("Введите возрастной лимит: ");
        List<Author> authors = authorService.getAuthors();
        List<Genre> genres = genreService.getGenres();

        try {
            bookDao.insert(new Book(null, title, copies, publisher, yearPublishing, printing, ageLimit,
                    authors, genres));
            io.println("Книга добавлена в библиотеку.");
        } catch (DataAccessException e) {
            io.println("Не удалось добавить книгу");
        }

    }
}
