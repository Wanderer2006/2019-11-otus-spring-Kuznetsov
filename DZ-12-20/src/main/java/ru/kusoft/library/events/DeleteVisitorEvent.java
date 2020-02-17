package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteVisitorEvent extends ApplicationEvent {

    @Getter
    private Long id;

    public DeleteVisitorEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
