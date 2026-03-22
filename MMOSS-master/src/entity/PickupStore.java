package entity;

/** An entity class which represents
 * a Pickup Store where customers can
 * collect their orders. It stores the
 * store name, address, phone number,
 * and business hours.
 *
 * @author Rebecca Chalmers
 * @version 1.0
 */
public class PickupStore
{
    private String name;
    private Address address;
    private String phone;
    private String businessHrs;

    /**
     * Non-Default Constructor for PickupStore class.
     * Creates a new PickupStore with its contact and location details.
     *
     * @param name         The name of the store.
     * @param phone        The contact phone number of the store.
     * @param address      The Address of the store.
     * @param businessHrs  The business hours as a String.
     */
    public PickupStore(String name,
                       String phone,
                       Address address,
                       String businessHrs)
    {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.businessHrs = businessHrs;
    }

    /**
     * Accessor Method for PickupStore to get the address.
     *
     * @return Address  The store address.
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * Accessor Method for PickupStore to get the business hours.
     *
     * @return String  The store business hours.
     */
    public String getBusinessHrs()
    {
        return businessHrs;
    }

    /**
     * Accessor Method for PickupStore to get the store name.
     *
     * @return String  The store name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Accessor Method for PickupStore to get the phone number.
     *
     * @return String  The store phone number.
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * Mutator Method for PickupStore to set the address.
     *
     * @param address  The new Address of the store.
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Mutator Method for PickupStore to set the business hours.
     *
     * @param businessHrs  The new business hours as a String.
     */
    public void setBusinessHrs(String businessHrs)
    {
        this.businessHrs = businessHrs;
    }

    /**
     * Mutator Method for PickupStore to set the store name.
     *
     * @param name  The new store name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mutator Method for PickupStore to set the phone number.
     *
     * @param phone  The new store phone number.
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}