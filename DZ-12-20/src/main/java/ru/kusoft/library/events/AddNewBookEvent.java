package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class AddNewBookEvent extends ApplicationEvent {

    public AddNewBookEvent(Object source) {
        super(source);
    }
}
