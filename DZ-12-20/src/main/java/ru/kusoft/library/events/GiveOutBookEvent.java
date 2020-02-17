package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.kusoft.library.domain.Relation;

public class GiveOutBookEvent extends ApplicationEvent {

    @Getter
    private Relation relation;

    public GiveOutBookEvent(Object source, Relation relation) {
        super(source);
        this.relation = relation;
    }
}
