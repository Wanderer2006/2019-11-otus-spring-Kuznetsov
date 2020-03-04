package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.kusoft.library.dao.ext.Relation;

public class ReturnBookEvent extends ApplicationEvent {

    @Getter
    private Relation relation;

    public ReturnBookEvent(Object source, Relation relation) {
        super(source);
        this.relation = relation;
    }
}
