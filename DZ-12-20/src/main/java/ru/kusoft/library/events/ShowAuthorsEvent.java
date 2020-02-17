package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowAuthorsEvent extends ApplicationEvent {

    public ShowAuthorsEvent(Object source) {
        super(source);
    }
}
