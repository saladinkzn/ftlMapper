package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.mapper.Property;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author sala
 */
@MappedType
public class ExtraInfo {
    @Property("extraInfo")
    private String extraField;

    public String getExtraField() {
        return extraField;
    }
}
