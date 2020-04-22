package ru.kusoft.library.dao.ext;

import lombok.Data;

@Data
public class Relation {
    private final long leftId;
    private final long rightId;

}
