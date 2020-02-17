package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class AddNewGenreEvent extends ApplicationEvent {

    public AddNewGenreEvent(Object source) {
        super(source);
    }
}
