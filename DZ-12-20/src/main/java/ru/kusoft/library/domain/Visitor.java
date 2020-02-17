package ru.kusoft.library.domain;

import lombok.Data;

@Data
public class Visitor {
    private final Long visitorId;
    private final String lastName;
    private final String firstName;
    private final String secondName;
    private final int age;

}
