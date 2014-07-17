package entity.property;

import java.util.List;

/**
 *
 */
public class Master2 {
    private long id;
    private String name;
    private List<Slave2> slaves;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Slave2> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<Slave2> slaves) {
        this.slaves = slaves;
    }
}
