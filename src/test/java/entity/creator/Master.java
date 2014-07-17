package entity.creator;

import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;

/**
 *
 */
public class Master {
    private final long id;
    private final String name;

    @Creator
    public Master(@Column("id") long id, @Column("name") String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
