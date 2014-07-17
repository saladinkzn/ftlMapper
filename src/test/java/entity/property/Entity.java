package entity.property;

import ru.shadam.ftlmapper.annotations.Column;

/**
 * @author sala
 */
public class Entity {
    @Column("entity_id")
    public long id;

    private String name;

    @Column("entity_name")
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
