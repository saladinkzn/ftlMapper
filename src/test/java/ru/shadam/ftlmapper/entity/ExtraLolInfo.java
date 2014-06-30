package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.mapper.Property;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author sala
 */
@MappedType
public class ExtraLolInfo extends PropertyLolInfo {
    @Property("extraInfo")
    private String extraInfo;

    public String getExtraInfo() {
        return extraInfo;
    }

    @Override
    public String toString() {
        return "ExtraLolInfo{" +
                "extraInfo='" + extraInfo + '\'' +
                '}';
    }
}
