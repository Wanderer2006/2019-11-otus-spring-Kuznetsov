package ru.kusoft.library.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.kusoft.library.dao.VisitorDao;
import ru.kusoft.library.dao.ext.RelationHelper;
import ru.kusoft.library.domain.Visitor;

import java.util.List;

@Service
public class VisitorServiceImpl implements VisitorService {
    private static final String FORMAT_HEADER = "%5s|%-40.40s%n";
    private static final String FORMAT_RECORD = "%5d|%-40.40s%n";
    private static final String VISITOR_BOOK_NAME_RELATION_TABLE = "visitor_book";

    private final VisitorDao visitorDao;
    private final IOService io;
    private final RelationHelper visitorBookRelation;

    public VisitorServiceImpl(VisitorDao visitorDao, IOService io, RelationHelper visitorBookRelation) {
        this.visitorDao = visitorDao;
        this.io = io;
        this.visitorBookRelation = visitorBookRelation;
        this.visitorBookRelation.setNameRelationTable(VISITOR_BOOK_NAME_RELATION_TABLE);
    }

    @Override
    public void showVisitors() {
        List<Visitor> visitors = visitorDao.getAll();
        io.print(FORMAT_HEADER, "Номер", "Посетитель и его возраст");
        visitors.forEach(v -> {
            String visitorStr = v.getLastName() + " " + v.getFirstName() + " " + v.getSecondName() + " - " + v.getAge() + " лет";
            io.print(FORMAT_RECORD, v.getVisitorId(), visitorStr);
        });
    }

    @Override
    public void deleteVisitorById(Long id) {
        if (visitorBookRelation.countByLeftId(id) > 0) {
            io.println("Нельзя удалить Посетителя, у которого на руках книги. Сначала сдайте книги в библиотеку");
        } else {
            try {
                visitorDao.deleteById(id);
                io.println("Посетитель удален");
            } catch (DataAccessException e) {
                io.println("Не удалось удалить посетителя");
            }
        }
    }

    @Override
    public void addNewVisitor() {
        String lastName = io.inputStringWithPrompt("Введите Фамилию посетителя: ");
        String firstName = io.inputStringWithPrompt("Введите Имя посетителя: ");
        String secondName = io.inputStringWithPrompt("Введите Отчество посетителя: ");
        int age = io.inputIntWithPrompt("Введите возраст: ");
        Visitor newVisitor = new Visitor(null, lastName, firstName, secondName, age);
        if (visitorDao.existVisitor(newVisitor)) {
            Visitor existVisitor = visitorDao.getByVisitor(newVisitor);
            io.println("Подобный посетитель уже существует - %s %s %s - %d лет",
                    existVisitor.getLastName(), existVisitor.getFirstName(), existVisitor.getSecondName(),
                    existVisitor.getAge());
        } else {
            try {
                visitorDao.insert(newVisitor);
                io.println("Посетитель добавлен");
            } catch (DataAccessException e) {
                io.println("Не удалось добавить посетителя");
            }
        }
    }
}
