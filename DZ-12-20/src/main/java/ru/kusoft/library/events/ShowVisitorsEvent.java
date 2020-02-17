package ru.kusoft.library.events;

import org.springframework.context.ApplicationEvent;

public class ShowVisitorsEvent extends ApplicationEvent {

    public ShowVisitorsEvent(Object source) {
        super(source);
    }
}
