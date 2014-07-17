package ru.shadam.ftlmapper.entity;

import ru.shadam.annotations.Column;
import ru.shadam.annotations.Creator;
import ru.shadam.ftlmapper.annotations.query.MappedType;

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
    public ConstructorExtraLolInfo(@Column("id") long id, @Column("name") String name, @Column("extraInfo") String extraInfo) {
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
