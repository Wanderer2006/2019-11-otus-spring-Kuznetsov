package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteAuthorEvent extends ApplicationEvent {

    @Getter
    private Long id;

    public DeleteAuthorEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
