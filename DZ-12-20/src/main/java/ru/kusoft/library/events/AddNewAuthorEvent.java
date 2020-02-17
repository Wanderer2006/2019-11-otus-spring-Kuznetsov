package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class AddNewAuthorEvent extends ApplicationEvent {

    public AddNewAuthorEvent(Object source) {
        super(source);
    }
}
