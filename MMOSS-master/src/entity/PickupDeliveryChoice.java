package entity;

/** An entity class which represents
 * the choice type for either
 * Delivery or Pickup in an Order.
 * It is a parent class of Delivery
 * and Pickup.
 * It stores the name of the choice,
 * such as "Delivery" or "Pickup".
 *
 * @author Rebecca Chalmers
 * @version 1.0
 */
public class PickupDeliveryChoice
{
    private String choiceName;

    /**
     * Non-Default Constructor for PickupDeliveryChoice class.
     * Creates a new choice option.
     *
     * @param choiceName   The name of the choice as a String.
     */
    public PickupDeliveryChoice(String choiceName)
    {
        this.choiceName = choiceName;
    }

    /**
     * Accessor Method for PickupDeliveryChoice to get the choice name.
     *
     * @return String  The name of the choice ("Delivery" or "Pickup").
     */
    public String getChoiceName()
    {
        return choiceName;
    }

    /**
     * Mutator Method for PickupDeliveryChoice to set the choice name.
     *
     * @param choiceName  The new name of the choice ("Delivery" or "Pickup").
     */
    public void setChoiceName(String choiceName)
    {
        this.choiceName = choiceName;
    }
}