package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** An entity class that represents a shopping Order.
 * It has the order details, including items, pricing,
 * delivery/pickup choice and confirmation status.
 *
 * @author Rebecca Chalmers
 * @version 1.0
 */
public class Order
{
    private int orderNo;
    private LocalDateTime createdAt;
    private List<CartItem> orderItems = new ArrayList<>();
    private PickupDeliveryChoice pickupDeliveryChoice;
    private Promotion promo;
    private double subtotal;
    private double discountTotal;
    private double totalPayable;
    private boolean confirmed;

    /**
     * Non-Default Constructor for Order class.
     * Creates a new Order.
     *
     * @param  orderNo   The order number (int).
     */
    public Order(int orderNo)
    {
        this.orderNo = orderNo;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Accessor Method for Order to get created at time.
     *
     * @return  LocalDateTime   The date-time when the order was created.
     */
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    /**
     * Accessor Method for Order to get discount total.
     *
     * @return  double   The total discount amount as double.
     */
    public double getDiscountTotal()
    {
        return discountTotal;
    }

    /**
     * Accessor Method for Order to get order items.
     *
     * @return  List&lt;CartItem&gt;   The list of cart items in the order.
     */
    public List<CartItem> getOrderItems()
    {
        return orderItems;
    }

    /**
     * Accessor Method for Order to get order number.
     *
     * @return  int   The order number as int.
     */
    public int getOrderNo()
    {
        return orderNo;
    }

    /**
     * Accessor Method for Order to get pickup/delivery choice.
     *
     * @return  PickupDeliveryChoice   The delivery or pickup choice object.
     */
    public PickupDeliveryChoice getPickupDeliveryChoice()
    {
        return pickupDeliveryChoice;
    }

    /**
     * Accessor Method for Order to get promotion.
     *
     * @return  Promotion   The applied promotion, or null if none.
     */
    public Promotion getPromo()
    {
        return promo;
    }

    /**
     * Accessor Method for Order to get subtotal.
     *
     * @return  double   The subtotal before discounts/fees.
     */
    public double getSubtotal()
    {
        return subtotal;
    }

    /**
     * Accessor Method for Order to get total payable.
     *
     * @return  double   The final payable amount as double.
     */
    public double getTotalPayable()
    {
        return totalPayable;
    }

    /**
     * Accessor Method for Order to check confirmation status.
     *
     * @return  boolean   True if order is confirmed, else false.
     */
    public boolean isConfirmed()
    {
        return confirmed;
    }

    /**
     * Mutator Method for Order to set confirmation status.
     *
     * @param  confirmed   True to mark order confirmed, else false.
     */
    public void setConfirmed(boolean confirmed)
    {
        this.confirmed = confirmed;
    }

    /**
     * Mutator Method for Order to set the creation date and time.
     *
     * @param createdAt  The LocalDateTime when the order was created.
     */
    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    /**
     * Mutator Method for Order to set discount total.
     *
     * @param  discountTotal   The total discount amount as double.
     */
    public void setDiscountTotal(double discountTotal)
    {
        this.discountTotal = discountTotal;
    }

    /**
     * Mutator Method for Order to set order items.
     *
     * @param  orderItems   The list of cart items to set.
     */
    public void setOrderItems(List<CartItem> orderItems)
    {
        this.orderItems = orderItems;
    }

    /**
     * Mutator Method for Order to set the order number.
     *
     * @param orderNo  The unique order number as an int.
     */
    public void setOrderNo(int orderNo)
    {
        this.orderNo = orderNo;
    }

    /**
     * Mutator Method for Order to set pickup/delivery choice.
     *
     * @param  pickupDeliveryChoice   The delivery or pickup choice object.
     */
    public void setPickupDeliveryChoice(PickupDeliveryChoice pickupDeliveryChoice)
    {
        this.pickupDeliveryChoice = pickupDeliveryChoice;
    }

    /**
     * Mutator Method for Order to set a promotion.
     *
     * @param  promo   The promotion to apply to the order.
     */
    public void setPromo(Promotion promo)
    {
        this.promo = promo;
    }

    /**
     * Mutator Method for Order to set subtotal.
     *
     * @param  subtotal   The subtotal amount as double.
     */
    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }

    /**
     * Mutator Method for Order to set total payable.
     *
     * @param  totalPayable   The final payable amount as double.
     */
    public void setTotalPayable(double totalPayable)
    {
        this.totalPayable = totalPayable;
    }
}
