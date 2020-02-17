package ru.kusoft.library.domain;

import lombok.Data;

@Data
public class Author {
    private final Long authorId;
    private final String lastName;
    private final String firstName;
    private final String secondName;

}
