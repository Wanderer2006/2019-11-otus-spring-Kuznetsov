package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class AddNewPublisherEvent extends ApplicationEvent {

    public AddNewPublisherEvent(Object source) {
        super(source);
    }
}
