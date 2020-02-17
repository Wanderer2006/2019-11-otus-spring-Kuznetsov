package ru.kusoft.library.domain;

import lombok.Data;

@Data
public class Relation {
    private final long leftId;
    private final long rightId;

}
