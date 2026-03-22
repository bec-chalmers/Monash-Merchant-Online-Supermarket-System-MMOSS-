package boundry;

import controllers.PickupController;
import entity.*;
import util.BannerUtil;
import java.util.Scanner;
import static util.ErrorUtil.showErrorScreen;

/**
 * Boundary class that handles displaying
 * the delivery and pickup choice to the user
 * and collecting their input.
 *
 * @version 1.7
 * @author Rebecca Chalmers
 */
public class DeliveryPickupView
{
    private static final double NON_STUDENT_DELIVERY_FEE = 20.00;
    private static final double STUDENT_DELIVERY_FEE = 0.00;
    private static final int PICKUP_DISCOUNT_PERCENTAGE = 5;

    private static final String CHOICE_DELIVERY = "Delivery";
    private static final String CHOICE_PICKUP = "Pickup";

    private final Customer customer;
    private final PickupController pickupController;
    private final Order order;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Non-Default Constructor for DeliveryPickupView class.
     * Creates the view for choosing Delivery or Pickup.
     *
     * @param customer         the logged-in customer
     * @param pickupController controller to fetch pickup store details
     * @param order            the current order being built
     */
    public DeliveryPickupView(Customer customer,
                              PickupController pickupController,
                              Order order)
    {
        this.customer = customer;
        this.pickupController = pickupController;
        this.order = order;
    }

    /**
     * Shows the Delivery vs Pickup options and
     * returns the result. Loops until the user
     * enters a valid option or chooses to go back.
     *
     * @return  "DELIVERY" if the user confirms delivery
     *          "PICKUP" if the user confirms pickup
     *          "BACK" if the user goes back
     */
    public String showDeliveryPickupOptions()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Delivery and Pickup");

        while (true)    // Continues prompting until valid input received
        {
            // Delivery/Pickup choice options + fee information
            // This part of the code is AI generated
            System.out.print("How would you like to receive your order?\n");
            System.out.println("\tDelivery:");
            System.out.println("\t\t* Monash Students: $" + String.format("%.2f", STUDENT_DELIVERY_FEE) + " delivery fee");
            System.out.println("\t\t* Other: $" + String.format("%.2f", NON_STUDENT_DELIVERY_FEE) + " delivery fee");
            System.out.println("\tPickup:");
            System.out.println("\t\t* Monash students receive a " + PICKUP_DISCOUNT_PERCENTAGE
                    + "% order discount when picking up their");
            System.out.println("\t\t  order (Cannot be combined with promo code discounts)\n");

            // Menu Options
            System.out.println("1. Delivery");
            System.out.println("2. Pickup");
            System.out.println("3. Go back to home\n");
            System.out.print("Enter your choice (1-3): ");

            // Validates numeric input
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1, 2, 3).");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)        // User selects "1. Delivery"
            {
                return chooseDeliveryPickupFlow(CHOICE_DELIVERY);
            }
            else if (choice == 2)   // User selects "2. Pickup"
            {
                return chooseDeliveryPickupFlow(CHOICE_PICKUP);
            }
            else if (choice == 3)   // User selects "3. Go back to home"
            {
                return "BACK";
            }
            else                    // User entered invalid number
            {
                showErrorScreen("Please enter a number (1, 2, 3).");
            }
        }
    }

    /**
     * Handles the confirmation screen for the delivery/pickup
     * choice chosen and if confirmed it is applied to the Order.
     * If user chooses delivery, the customer's saved address is displayed
     * If user chooses pickup, the store information is retrieved and displayed
     *
     * @param type  "Delivery" or "Pickup"
     * @return      "DELIVERY" or "PICKUP" when the user confirms
     *              or "BACK" if user cancels
     */
    private String chooseDeliveryPickupFlow(String type)
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader(type);

        if (type.equalsIgnoreCase(CHOICE_DELIVERY))  // User chose Delivery
        {
            // Gets customer address from the Customer entity
            Address customerAddress = customer.getAddress();

            // Checks for address (cannot proceed with Delivery if no address)
            if (customerAddress == null)
            {
                showErrorScreen("Address is null.");
                return "BACK";
            }

            System.out.println("Your order will be delivered to: " + customerAddress + ".\n");
        }
        else    // User chose Pickup
        {
            PickupStore pickupStore = pickupController.getPickupStore();

            // Checks for PickupStore (cannot proceed with Pickup if no pickup store)
            if (pickupStore == null)
            {
                showErrorScreen("Pickup Store is invalid.");
                return "BACK";
            }

            // Displays the store details to the user
            System.out.println("Pickup store details:");
            System.out.println("\tStore name: " + pickupStore.getName());
            System.out.println("\tStore address: " + pickupStore.getAddress());
            System.out.println("\tPhone number: " + pickupStore.getPhone());
            System.out.println("\tBusiness hours: " + pickupStore.getBusinessHrs());
            System.out.println("\nThis information will be available after checkout.\n");
        }

        // Delivery/Pickup selection confirmation screen
        System.out.println("\t1. Confirm");
        System.out.println("\t2. Go back\n");
        System.out.print("Enter your choice (1, 2): ");

        while (true)
        {
            // User did not enter a number
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1 or 2).");
                System.out.print("Enter your choice (1, 2): ");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)        // User selects "1. Confirm"
            {
                // Confirming Delivery option
                if (type.equalsIgnoreCase(CHOICE_DELIVERY))
                {
                    // Validates address again
                    Address address = customer.getAddress();

                    if (address == null)
                    {
                        showErrorScreen("No delivery address on profile");
                        return "BACK";
                    }

                    // Monash students pay $0 delivery fee, everyone else $20
                    double deliveryFee = customer.isStudent() ? STUDENT_DELIVERY_FEE : NON_STUDENT_DELIVERY_FEE;

                    // Saves choice of Delivery to the Order
                    order.setPickupDeliveryChoice(new Delivery(address, deliveryFee));
                    return "DELIVERY";
                }
                // Confirming Pickup Option
                else
                {
                    // Gets the most updated pickup store
                    PickupStore pickupStore = pickupController.getPickupStore();

                    if (pickupStore == null)
                    {
                        showErrorScreen("Pickup Store is invalid.");
                        return "BACK";
                    }

                    // Saves choice of Pickup to the Order
                    order.setPickupDeliveryChoice(new Pickup(pickupStore, PICKUP_DISCOUNT_PERCENTAGE));
                    return "PICKUP";
                }
            }
            else if (choice == 2)   // User selects "2. Go back"
            {
                return "BACK";
            }
            else                    // User entered an invalid number
            {
                showErrorScreen("Please enter a number (1 or 2).");
                System.out.print("Enter your choice (1, 2): ");
            }
        }
    }
}