package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.mapper.annotations.Creator;
import ru.shadam.ftlmapper.mapper.annotations.Property;
import ru.shadam.ftlmapper.query.annotations.MappedType;

/**
 * @author sala
 */
@MappedType
public class ConstructorExtraLolInfo extends CreatorLolInfo {
    private final String extraInfo;

    public String getExtraInfo() {
        return extraInfo;
    }

    @Creator
    public ConstructorExtraLolInfo(@Property("id") long id, @Property("name") String name, @Property("extraInfo") String extraInfo) {
        super(id, name);
        this.extraInfo = extraInfo;
    }

    @Override
    public String toString() {
        return "ConstructorExtraLolInfo{" +
                "extraInfo='" + extraInfo + '\'' +
                '}';
    }
}
