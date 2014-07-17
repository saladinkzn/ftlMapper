package entity.inherit;

import entity.Address;
import entity.Slave;

/**
 *
 */
public class SlaveWithAddressProperty extends Slave {
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
