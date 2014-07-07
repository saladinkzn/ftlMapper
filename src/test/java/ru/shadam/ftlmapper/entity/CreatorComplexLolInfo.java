package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.mapper.Creator;
import ru.shadam.ftlmapper.annotations.mapper.Embedded;
import ru.shadam.ftlmapper.annotations.mapper.Property;
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
    public CreatorComplexLolInfo(@Property("id") long id, @Property("name") String name, @Embedded("extra_") ExtraInfo extraInfo) {
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
