package entity.inherit;

import entity.Address;
import entity.Slave;
import ru.shadam.ftlmapper.annotations.Column;
import ru.shadam.ftlmapper.annotations.Creator;

/**
 *
 */
public class SlaveWithAddressCreator extends Slave {
    private final Address address;

    @Creator
    public SlaveWithAddressCreator(@Column("id") long id, @Column("name") String name, @Column("address") Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}
