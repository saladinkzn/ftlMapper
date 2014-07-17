package entity.creator;

import entity.Address;
import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;

/**
 *
 */
public class Person {
    private final long id;
    private final String name;
    private final Address address;

    @Creator
    public Person(@Column("id") long id, @Column("name") String name, @Column("address") Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
