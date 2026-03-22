package boundry;
import controllers.PickupController;
import entity.Customer;
import entity.Order;
import entity.Pickup;
import entity.PickupStore;
import util.BannerUtil;

import java.util.Scanner;

/**
 * Boundary class that handles display of order confirmation
 * to the user, which includes a confirmation message, the
 * customer's new balance, and delivery/pickup information.
 * If delivery, shows delivery address
 * If pickup, shows the details of the pickup store
 *
 * @version 1.1
 * @author Rebecca Chalmers
 */
public class OrderConfirmationView
{
    private final PickupController pickupController;
    private final Customer customer;
    private final Order order;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Non-Default Constructor for OrderConfirmationView class.
     * Creates the view for showing the order confirmation summary.
     *
     * @param pickupController controller to fetch pickup store details
     * @param customer         the logged-in customer
     * @param order            the order that has just been confirmed
     */
    public OrderConfirmationView(PickupController pickupController, Customer customer, Order order)
    {
        this.pickupController = pickupController;
        this.customer = customer;
        this.order = order;
    }

    /**
     * Displays the order confirmation screen.
     * Prints a header with the order number, a confirmation message,
     * the customer's current balance, and either delivery or pickup details
     * depending on the user's previous choice.
     * Waits for the user to press Enter before returning to the caller.
     */
    public void showOrderConfirmationScreen()
    {
        BannerUtil.printHeader();

        // Displays header including order number
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("Order Confirmation%60s%d%n", "Order No. ", order.getOrderNo());
        System.out.println("---------------------------------------------------------------------------------");

        System.out.println("Your order has been confirmed!");

        // Shows current account balance to 2 decimal places
        // AI was used to generate this code
        System.out.printf("Current account balance: AUD $%.2f%n%n", customer.getBalance());

        // Shows different information depending on if order is delivery or pickup
        boolean isPickup = order.getPickupDeliveryChoice() instanceof Pickup;

        // Pickup summary information
        if (isPickup) {
            System.out.println("\tPickup Store Details:");

            // Retrieves the currently selected pickup store from the controller
            PickupStore store = pickupController.getPickupStore();

            // Displays store details
            if (store != null)
            {
                System.out.println("\t\t" + store.getName());
                System.out.println("\t\t" + store.getAddress());
                System.out.println("\t\tPhone: " + store.getPhone());
                System.out.println("\t\tBusiness hours: " + store.getBusinessHrs());
            }
            else
            {
                System.out.println("\t\t(Pickup store details unavailable)");
            }
        }

        // Delivery summary information
        else
        {
            System.out.println("\tDelivery Details:");

            // Uses the customer's saved address
            if (customer.getAddress() != null)
            {
                System.out.println("\t\tDelivery address: " + customer.getAddress());
            }
            // if customer address not provided, displays this
            else
            {
                System.out.println("\t\t(Delivery address unavailable)");
            }
        }

        // Thank you message
        System.out.println();
        System.out.println("Thank you for using Monash Merchant Online Supermarket System.");
        System.out.print("\nPress Enter to return to home...");

        scanner.nextLine();
    }
}
