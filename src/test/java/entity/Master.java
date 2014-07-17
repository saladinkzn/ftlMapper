package entity;

import java.util.List;

/**
 * @author Timur Shakurov
 */
public class Master {
    public long id;
    public String name;
    public List<Slave> slaves;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Slave> getSlaves() {
        return slaves;
    }
}
