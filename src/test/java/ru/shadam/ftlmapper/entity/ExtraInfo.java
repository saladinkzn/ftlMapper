package ru.shadam.ftlmapper.entity;

import ru.shadam.annotations.Column;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author sala
 */
@MappedType
public class ExtraInfo {
    @Column("extraInfo")
    private String extraField;

    public String getExtraField() {
        return extraField;
    }
}
