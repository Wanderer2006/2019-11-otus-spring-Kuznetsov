package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowPublishersEvent extends ApplicationEvent {

    public ShowPublishersEvent(Object source) {
        super(source);
    }
}
