package ru.kusoft.library.service;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.kusoft.library.dao.GenreDao;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Genre;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class GenreServiceImpl implements GenreService {
    private static final String FORMAT_HEADER = "%5s|%-40.40s%n";
    private static final String FORMAT_RECORD = "%5d|%-40.40s%n";
    private static final String BOOK_GENRE_NAME_RELATION_TABLE = "book_genre";

    private final GenreDao genreDao;
    private final IOService io;
    private final RelationHelper bookGenreRelation;

    @PostConstruct
    public void setNameRelationTable() {
        bookGenreRelation.setNameRelationTable(BOOK_GENRE_NAME_RELATION_TABLE);
    }

    @Override
    public void showGenres() {
        List<Genre> genres = genreDao.getAll();
        io.print(FORMAT_HEADER, "Номер", "Жанр");
        genres.forEach(g -> {
            io.print(FORMAT_RECORD, g.getGenreId(), g.getGenre());
        });
    }

    @Override
    public void deleteGenreById(Long id) {
        if (bookGenreRelation.countByRightId(id) > 0) {
            io.println("Нельзя удалить жанр книги, принадлежащей библиотеке. Сначала удалите книгу");
        } else {
            try {
                genreDao.deleteById(id);
                io.println("Жанр удален");
            } catch (DataAccessException e) {
                io.println("Не удалось удалить жанр");
            }
        }
    }

    @Override
    public void addNewGenre() {
        String genre = io.inputStringWithPrompt("Введите наименование жанра: ");
        if (genreDao.existByName(genre)) {
            io.println("Жанр с подобным наименованием уже существует - %s", genreDao.getByName(genre).getGenre());
        } else {
            try {
                genreDao.insert(new Genre(null, genre));
                io.println("Жанр добавлен");
            } catch (DataAccessException e) {
                io.println("Не удалось добавить жанр");
            }
        }
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();
        showGenres();
        io.println("Вводите номера Жанров(0 - закончить ввод)");
        Long genreId = Long.valueOf(0);
        while (genreId != 0 || genres.size() == 0) {
            genreId = io.inputLongWithPrompt("Введите номера Жанра: ");
            if (genreId == 0 && genres.size() == 0) {
                io.println("Необходимо указать хотя бы один жанр");
            } else if (genreId != 0) {
                if (!genreDao.existById(genreId)) {
                    io.print("Вы ввели номер несуществующего Жанра! ");
                } else {
                    genres.add(genreDao.getById(genreId));
                }
            }
        }

        return genres;
    }
}
