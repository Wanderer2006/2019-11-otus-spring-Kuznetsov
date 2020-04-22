package ru.kusoft.library.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.kusoft.library.dao.AuthorDao;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final String FORMAT_HEADER = "%5s|%-40.40s%n";
    private static final String FORMAT_RECORD = "%5d|%-40.40s%n";
    private static final String BOOK_AUTHOR_NAME_RELATION_TABLE = "book_author";

    private final AuthorDao authorDao;
    private final IOService io;
    private final RelationHelper bookAuthorRelation;

    public AuthorServiceImpl(AuthorDao authorDao, IOService io, RelationHelper bookAuthorRelation) {
        this.authorDao = authorDao;
        this.io = io;
        this.bookAuthorRelation = bookAuthorRelation;
        this.bookAuthorRelation.setNameRelationTable(BOOK_AUTHOR_NAME_RELATION_TABLE);
    }

    @Override
    public void showAuthors() {
        List<Author> authors = authorDao.getAll();
        io.print(FORMAT_HEADER, "Номер", "Автор");
        authors.forEach(a -> {
            String authorsStr = a.getSecondName() != null ? a.getLastName() + " " + a.getFirstName() + " " + a.getSecondName()
                                : a.getFirstName() + " " + a.getLastName();
            io.print(FORMAT_RECORD, a.getAuthorId(), authorsStr);
        });
    }

    @Override
    public void deleteAuthorById(Long id) {
        if (bookAuthorRelation.countByRightId(id) > 0) {
            io.println("Нельзя удалить автора книги, принадлежащей библиотеке. Сначала удалите книгу");
        } else {
            try {
                authorDao.deleteById(id);
                io.println("Автор удален");
            } catch (DataAccessException e) {
                io.println("Не удалось удалить автора");
            }
        }
    }

    @Override
    public void addNewAuthor() {
        String lastName = io.inputStringWithPrompt("Введите Фамилию автора: ");
        String firstName = io.inputStringWithPrompt("Введите Имя автора: ");
        String secondName = io.inputStringWithPrompt("Введите Отчество автора (если есть): ");
        Author newAuthor = new Author(null, lastName, firstName, isNotBlank(secondName) ? secondName : null);
        if (authorDao.existAuthor(newAuthor)) {
            Author existAuthor = authorDao.getByAuthor(newAuthor);
            io.println("Подобный автор уже существует - %s %s %s", existAuthor.getLastName(), existAuthor.getFirstName(),
                    isNotBlank(existAuthor.getSecondName()) ? existAuthor.getSecondName() : "");
        } else {
            try {
                authorDao.insert(newAuthor);
                io.println("Автор добавлен");
            } catch (DataAccessException e) {
                io.println("Не удалось добавить автора");
            }
        }
    }

    @Override
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        showAuthors();
        io.println("Вводите номера Авторов(0 - закончить ввод)");
        Long authorId = Long.valueOf(0);
        while (authorId != 0 || authors.size() == 0) {
            authorId = io.inputLongWithPrompt("Введите номер Автора: ");
            if (authorId == 0 && authors.size() == 0) {
                io.println("Необходимо указать хотя бы одного автора");
            } else if (authorId != 0) {
                if (!authorDao.existById(authorId)) {
                    io.print("Вы ввели номер несуществующего Автора! ");
                } else {
                    authors.add(authorDao.getById(authorId));
                }
            }
        }

        return authors;
    }
}
