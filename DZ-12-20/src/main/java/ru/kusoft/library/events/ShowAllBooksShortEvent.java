package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowAllBooksShortEvent extends ApplicationEvent {

    public ShowAllBooksShortEvent(Object source) {
        super(source);
    }
}
