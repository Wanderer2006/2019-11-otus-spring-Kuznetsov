package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowAllBooksFullEvent extends ApplicationEvent {

    public ShowAllBooksFullEvent(Object source) {
        super(source);
    }
}
