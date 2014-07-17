package entity.property;

import ru.shadam.annotations.Column;

import java.util.List;

/**
 * @author sala
 */
public class Master {
    public long id;

    @Column("slaves")
    public List<Entity> slaveList;
}
