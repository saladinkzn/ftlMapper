package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author sala
 */
@MappedType
public class CreatorComplexLolInfo {
    private long id;
    private String name;
    private ExtraInfo extraInfo;

    @Creator
    public CreatorComplexLolInfo(@Column("id") long id, @Column("name") String name, @Column("extra") ExtraInfo extraInfo) {
        this.id = id;
        this.name = name;
        this.extraInfo = extraInfo;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ExtraInfo getExtraInfo() {
        return extraInfo;
    }
}
