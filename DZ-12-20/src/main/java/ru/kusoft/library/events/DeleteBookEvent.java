package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteBookEvent extends ApplicationEvent {

    @Getter
    private Long id;

    public DeleteBookEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
