package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowGenresEvent extends ApplicationEvent {

    public ShowGenresEvent(Object source) {
        super(source);
    }
}
