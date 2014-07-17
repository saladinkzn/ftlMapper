package entity;

/**
 * @author sala
 */
public class Slave {
    public long id;
    public String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Slave{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
