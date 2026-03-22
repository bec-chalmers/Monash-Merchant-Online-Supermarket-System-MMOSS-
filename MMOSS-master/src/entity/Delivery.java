package entity;

/**
 * An entity class that represents a
 * choice of 'Delivery' for an Order.
 * It has a delivery address and delivery fee.
 * It is a subclass of PickupDeliveryChoice.
 */
public class Delivery extends PickupDeliveryChoice
{
    private Address deliveryAddress;
    private double deliveryFee;

    /**
     * Non-Default Constructor for Delivery class.
     * Creates a new Delivery option.
     *
     * @param deliveryAddress   The Address for orders to be delivered to
     * @param deliveryFee       The fee charged for delivery (double)
     */
    public Delivery(Address deliveryAddress, double deliveryFee)
    {
        super("Delivery");  // tells PickupDeliveryChoice that the choice is Delivery
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = deliveryFee;
    }

    /**
     * Accessor Method for Delivery to get delivery address.
     *
     * @return the address to deliver to
     */
    public Address getDeliveryAddress()
    {
        return deliveryAddress;
    }

    /**
     * Mutator Method for Delivery to set delivery address.
     *
     * @return The delivery fee (double).
     */
    public double getDeliveryFee()
    {
        return deliveryFee;
    }

    /**
     * Mutator Method for Delivery to set the delivery address.
     *
     * @param deliveryAddress  The new address where the order will be delivered.
     */
    public void setDeliveryAddress(Address deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Mutator Method for Delivery to set the delivery fee.
     *
     * @param deliveryFee  The fee charged for delivery as a double.
     */
    public void setDeliveryFee(double deliveryFee)
    {
        this.deliveryFee = deliveryFee;
    }
}