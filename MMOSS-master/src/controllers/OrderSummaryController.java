package controllers;

import entity.*;
import util.FileStorageUtil;
import java.io.IOException;
import java.util.List;

/**
 * Controller class that handles
 * calculations for the order summary.
 *
 * @version 1.7
 * @author Rebecca Chalmers
 */
public class OrderSummaryController
{
    private final ProductController productController;

    /**
     * Non-Default Constructor for OrderSummaryController class.
     *
     * @param productController controller that manages product inventory updates
     */
    public OrderSummaryController(ProductController productController)
    {
        this.productController = productController;
    }

    /**
     * Calculates a single line total for a cart item based on customer VIP status.
     * Uses member price if the customer is VIP; otherwise uses regular price.
     *
     * @param item      the cart item containing product and quantity
     * @param customer  the current customer (used to check VIP status)
     * @return          the extended price for this line (unit price × quantity)
     */
    public double calculateLineTotal(CartItem item, Customer customer)
    {
        // Choose unit price based on VIP status
        double unit = isVip(customer) ? item.getProduct().getMemberPrice() : item.getProduct().getPrice();
        return unit * item.getQuantity();
    }

    /**
     * Sums all line totals to derive the order subtotal (before discounts/fees).
     *
     * @param order    the order containing all items
     * @param customer the current customer (for VIP pricing)
     * @return subtotal of all items in the order
     */
    public double calculateSubtotal(Order order, Customer customer)
    {
        double sum = 0.00;

        for (CartItem item : order.getOrderItems())
        {
            sum += calculateLineTotal(item, customer);
        }

        return sum;
    }

    /**
     * Determines whether the customer currently has an active VIP membership.
     *
     * @param customer the customer to check
     * @return true if a non-null, active membership exists; otherwise false
     */
    public boolean isVip(Customer customer)
    {
        if (customer == null || customer.getMembershipHistory() == null)
        {
            return false;
        }

        for (VIPMembership membership : customer.getMembershipHistory())
        {
            if (membership != null && membership.isActive())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the pickup student discount.
     * 5% off for Monash students on Pickup orders,
     * but only if no promo code is applied. The actual discount
     * percentage is taken from the chosen Pickup option.
     *
     * @param subtotal the pre-discount subtotal
     * @param customer the current customer (used to verify student status)
     * @param order    the current order (used to verify pickup choice and promo)
     * @return the pickup discount amount to apply, or 0.00 if not applicable
     */
    public double calculateStudentPickupDiscount(double subtotal, Customer customer, Order order)
    {
        // Only applies to Pickup orders
        if (!(order.getPickupDeliveryChoice() instanceof Pickup pickup))
        {
            return 0.00;
        }

        // Not compatible with promo codes
        if (order.getPromo() != null)
        {
            return 0.00;
        }

        // Customer must be Monash student
        if (customer == null || !customer.isStudent())
        {
            return 0.00;
        }

        // If discount would be less than 0 the discount is 0
        int discount = pickup.getPickupDiscount();

        if (discount < 0)
        {
            discount = 0;
        }

        double rate = ((double) discount) / 100.00;

        return subtotal * rate;
    }

    /**
     * Calculates the promotion discount to be applied to the subtotal.
     *
     * @param subtotal the pre-discount subtotal
     * @param order    the current order (inspected for an applied promotion)
     * @return the discount amount to apply, or 0.00 if no promotion/invalid fields
     */
    public double calculatePromoDiscount(double subtotal, Order order)
    {
        Promotion promotion = order.getPromo();

        if (promotion == null) return 0.00;

        if (promotion.getPercentageOff() > 0)
        {
            return subtotal * (promotion.getPercentageOff() / 100.00);
        }

        if (promotion.getAmountOff() > 0)
        {
            return promotion.getAmountOff();
        }

        return 0.0;
    }

    /**
     * Chooses the discount to apply when both promotion and pickup discounts exist.
     * If both are present, returns the larger; otherwise returns the sum (i.e., one is zero).
     *
     * @param promotion the promotion discount amount
     * @param pickup    the pickup discount amount
     * @return the discount amount to apply
     */
    public double chooseHighestDiscount(double promotion, double pickup)
    {
        if (promotion > 0.00 && pickup > 0.00)
        {
            return Math.max(promotion, pickup);
        }

        // If only one of them is > 0, this sums to that one (the other is 0.00)
        return promotion + pickup;
    }

    /**
     * Retrieves the delivery fee if the order is a Delivery; 0.00 otherwise.
     *
     * @param order the current order
     * @return delivery fee amount, or 0.00 if not applicable
     */
    public double calculateDeliveryFee(Order order)
    {
        if (!(order.getPickupDeliveryChoice() instanceof Delivery delivery))
        {
            return 0.00;
        }

        return delivery.getDeliveryFee();
    }

    /**
     * Calculates the final total payable by the customer.
     * Formula: total = max(subtotal - chosenDiscount, 0.00) + deliveryFee
     *
     * @param order    the current order
     * @param customer the current customer (for VIP/student pricing rules)
     * @return the final amount due
     */
    public double calculateTotalPayable(Order order, Customer customer)
    {
        double subtotal       = calculateSubtotal(order, customer);
        double promoDiscount  = calculatePromoDiscount(subtotal, order);
        double pickupDiscount = calculateStudentPickupDiscount(subtotal, customer, order);
        double chosenDiscount = chooseHighestDiscount(promoDiscount, pickupDiscount);

        double discountedSubtotal = subtotal - chosenDiscount;

        // if the subtotal would go below $0.00 after applying discounts, it doesn't go negative
        if (discountedSubtotal < 0.00)
        {
            discountedSubtotal = 0.00;
        }

        double deliveryFee = calculateDeliveryFee(order);

        return discountedSubtotal + deliveryFee;
    }

    /**
     * Attempts to place the order:
     * Calculates total payable and charges the customer's balance
     * If successful, updates inventory via ProductController
     *
     * @param customer          the customer paying for the order
     * @param order             the order being placed
     * @param customerFilepath  path to the customers CSV (to persist balance updates)
     * @param productsFilepath  path to the products CSV (to persist inventory updates)
     * @return true if charged and inventory updated; false if insufficient funds
     * @throws IOException if persistence (customer or product update) fails
     */

    public boolean placeOrder(Customer customer,
                              Order order,
                              String customerFilepath,
                              String productsFilepath) throws IOException
    {
        // Calculates order total and tries to charge customer's account
        double total = Math.round(calculateTotalPayable(order, customer) * 100.0) / 100.0;
        boolean charged = chargeCustomer(customer, total, customerFilepath);

        // Order fails if customer has insufficient funds
        if (!charged)
        {
            return false;
        }

        // Updates store inventory
        productController.updateInventory(productsFilepath, order);

        return true;
    }

    /**
     * Charges the customer's account balance and persists the new balance.
     * If persistence fails, the in-memory balance is rolled back.
     *
     * @param customer         the customer to charge
     * @param total            the total to charge (assumed rounded to cents)
     * @param customerFilepath path to the customers CSV
     * @return true if the customer had sufficient funds and persistence succeeded; otherwise false
     * @throws IOException if writing the updated balance fails (balance is rolled back)
     */
    public boolean chargeCustomer(Customer customer, double total, String customerFilepath) throws IOException
    {
        // Reject if insufficient funds
        if (customer.getBalance() < total)
        {
            return false;
        }

        double oldBalance = customer.getBalance();
        customer.setBalance(Math.round((oldBalance - total) * 100.0) / 100.0);

        try
        {
            updateCustomerBalance(customerFilepath, customer);
            return true;
        }
        catch (IOException e)
        {
            customer.setBalance(oldBalance);
            throw e;
        }
    }

    /**
     * Updates only the balance column for the row matching the customer's email.
     * Rewrites the file with the updated rows and a fixed header line.
     *
     * @param path     path to the customers CSV
     * @param customer the customer whose balance must be updated
     * @throws IOException if reading or writing the file fails
     */
    private void updateCustomerBalance(String path, Customer customer) throws IOException
    {
        // Reads entire CSV
        java.util.List<java.util.List<String>> rows = FileStorageUtil.readMultiColumnFile(path);

        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add("email,password,first_name,last_name,mobile_number,date_of_birth,gender," +
                "building_number,street_number,street_name,suburb,state,postcode,is_student,balance");

        for (List<String> row : rows)
        {
            if (row.isEmpty())
            {
                continue;
            }

            // Matches to customer using their email
            if (row.getFirst().equalsIgnoreCase(customer.getEmail()))
            {
                // Validates row has at least 15 columns in it
                while (row.size() <= 14)
                {
                    row.add("");
                }

                // Writes the balance
                row.set(14, String.format("%.2f", customer.getBalance()));
            }

            lines.add(String.join(",", row));
        }

        FileStorageUtil.writeToFile(path, lines);
    }

    /**
     * Saves order details for a given customer and order into the existing
     *
     * @param customer Customer Obj linking the customer who placed the order.
     * @param order Order obj with all the order details.
     * @throws IOException if appending to the order history file fails
     */
    public void saveOrderInfo(Customer customer, Order order) throws IOException
    {

        String totalCost = String.valueOf(order.getTotalPayable());
        String orderDate = order.getCreatedAt().toString();
        String discountTotal = String.valueOf(order.getDiscountTotal());
        String pickupOrDelivery = order.getPickupDeliveryChoice().getChoiceName();
        String promoCode = (order.getPromo() != null) ? order.getPromo().getCode() : "";
        String customerEmail = customer.getEmail();
        String membershipType = (!customer.getMembershipHistory().isEmpty()) ? "VIPMember" : "";

        // Build item summary string: ProductName:UnitPrice:Quantity:ItemTotal
        StringBuilder itemsBuilder = new StringBuilder();
        for (CartItem item : order.getOrderItems()) {
            String productName = item.getProduct().getName();
            double unitPrice = item.getProduct().getPrice();
            int quantity = item.getQuantity();
            double itemTotal = unitPrice * quantity;

            itemsBuilder.append(productName)
                    .append(":").append(unitPrice)
                    .append(":").append(quantity)
                    .append(":").append(itemTotal)
                    .append(";");
        }

        // Remove trailing semicolon if exists
        String itemsSummary = (!itemsBuilder.isEmpty())
                ? itemsBuilder.substring(0, itemsBuilder.length() - 1)
                : "";

        // Build CSV line
        String lineForCSV = String.join(",",
                customerEmail,
                membershipType,
                orderDate,
                pickupOrDelivery,
                promoCode,
                discountTotal,
                totalCost,
                itemsSummary
        );

        FileStorageUtil.appendToFile("data/OrderHistory.txt", lineForCSV);
    }
}