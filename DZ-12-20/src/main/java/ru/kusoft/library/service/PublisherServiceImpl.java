package ru.kusoft.library.service;

import lombok.Data;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.kusoft.library.dao.PublisherDao;
import ru.kusoft.library.domain.Publisher;

import java.util.List;

@Data
@Service
public class PublisherServiceImpl implements PublisherService {
    private static final String FORMAT_HEADER = "%5s|%-40.40s%n";
    private static final String FORMAT_RECORD = "%5d|%-40.40s%n";

    private final PublisherDao publisherDao;
    private final IOService io;

    @Override
    public void showPublishers() {
        List<Publisher> publishers = publisherDao.getAll();
        io.print(FORMAT_HEADER, "Номер", "Издательство");
        publishers.forEach(p -> {
            io.print(FORMAT_RECORD, p.getPublisherId(), p.getPublisherName());
        });
    }

    @Override
    public void deletePublisherById(Long id) {
        if (publisherDao.existRelationById(id)) {
            io.println("Нельзя удалить Издательство книги, принадлежащей библиотеке. Сначала удалите книгу");
        } else {
            try {
                publisherDao.deleteById(id);
                io.println("Издательство удалено");
            } catch (DataAccessException e) {
                io.println("Не удалось удалить издательство");
            }
        }
    }

    @Override
    public void addNewPublisher() {
        String publisher = io.inputStringWithPrompt("Введите наименование издательства: ");
        if (publisherDao.existByName(publisher)) {
            io.println("Издательство с подобным наименованием уже существует - %s",
                    publisherDao.getByName(publisher).getPublisherName());
        } else {
            try {
                publisherDao.insert(new Publisher(null, publisher));
                io.println("Издательство добавлено");
            } catch (DataAccessException e) {
                io.println("Не удалось добавить издательство");
            }
        }
    }

    @Override
    public Publisher getPublisher() {
        showPublishers();
        Long publisherId = io.inputLongWithPrompt("Введите номер Издательства: ");
        while (!publisherDao.existById(publisherId)) {
            publisherId = io.inputLongWithPrompt("Вы ввели номер несуществующего Издательства! " +
                    "Еще раз введите номер Издательства: ");
        }

        return publisherDao.getById(publisherId);
    }
}
