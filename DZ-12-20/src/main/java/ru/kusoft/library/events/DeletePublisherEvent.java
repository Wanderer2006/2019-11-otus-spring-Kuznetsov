package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeletePublisherEvent extends ApplicationEvent {

    @Getter
    private Long id;

    public DeletePublisherEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
