package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class AddNewVisitorEvent extends ApplicationEvent {

    public AddNewVisitorEvent(Object source) {
        super(source);
    }
}
