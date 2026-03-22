package entity;

/** An entity class which represents
 * the choice of 'Pickup' for an Order.
 * It contains the pickup store details and a
 * pickup discount (as a percentage).
 *
 * @author Rebecca Chalmers
 * @version 1.0
 */
public class Pickup extends PickupDeliveryChoice
{
    private PickupStore pickupStore;
    private int pickupDiscount;

    /**
     * Non-Default Constructor for Pickup class.
     * Creates a new Pickup option.
     *
     * @param pickupStore     The PickupStore where the customer can collect the order from.
     * @param pickupDiscount  The pickup discount as an integer percent (0–100).
     */
    public Pickup(PickupStore pickupStore, int pickupDiscount)
    {
        super("Pickup");
        this.pickupStore = pickupStore;
        this.pickupDiscount = pickupDiscount;
    }

    /**
     * Accessor Method for Pickup to get the pickup discount.
     *
     * @return int  The pickup discount as a percent (0–100).
     */
    public int getPickupDiscount()
    {
        return pickupDiscount;
    }

    /**
     * Accessor Method for Pickup to get the pickup store.
     *
     * @return PickupStore  The store where the order can be collected from.
     */
    public PickupStore getPickupStore()
    {
        return pickupStore;
    }

    /**
     * Mutator Method for Pickup to set the pickup discount.
     *
     * @param pickupDiscount  The pickup discount as an integer percent (0–100).
     */
    public void setPickupDiscount(int pickupDiscount)
    {
        this.pickupDiscount = pickupDiscount;
    }

    /**
     * Mutator Method for Pickup to set the pickup store.
     *
     * @param pickupStore  The PickupStore where the order can be collected from.
     */
    public void setPickupStore(PickupStore pickupStore)
    {
        this.pickupStore = pickupStore;
    }
}