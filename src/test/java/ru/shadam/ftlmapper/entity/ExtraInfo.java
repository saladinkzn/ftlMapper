package ru.shadam.ftlmapper.entity;

import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.query.MappedType;

/**
 * @author sala
 */
@MappedType
public class ExtraInfo {
    @Column("extraInfo")
    public String extraField;
}
