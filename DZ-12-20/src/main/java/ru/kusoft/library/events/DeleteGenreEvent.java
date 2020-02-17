package ru.kusoft.library.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class DeleteGenreEvent extends ApplicationEvent {

    @Getter
    private Long id;

    public DeleteGenreEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
