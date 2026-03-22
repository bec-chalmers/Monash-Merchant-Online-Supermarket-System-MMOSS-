package boundry;

import controllers.CartController;
import controllers.OrderSummaryController;
import controllers.PickupController;
import controllers.PromoController;
import entity.*;
import util.BannerUtil;

import java.io.IOException;
import java.util.Scanner;

import static util.ErrorUtil.showErrorScreen;

/**
 * Boundary class that handles
 * the displaying of the order
 * summary to the user.
 *
 * @version 1.7
 * @author Rebecca Chalmers
 */

public class OrderSummaryView
{
    private final OrderSummaryController orderSummaryController;
    private final PickupController pickupController;
    private final PromoController promoController;
    private final Customer customer;
    private final Order order;
    private final Scanner scanner = new Scanner(System.in);

    public OrderSummaryView(OrderSummaryController orderSummaryController,
                            PickupController pickupController,
                            PromoController promoController,
                            Customer customer,
                            Order order)
    {
        this.orderSummaryController = orderSummaryController;
        this.pickupController = pickupController;
        this.promoController = promoController;
        this.customer = customer;
        this.order = order;
    }

    public void showOrderSummary()
    {
        showCustomerDetails();
    }

    public void showCustomerDetails()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Order Summary - Personal Details");

        System.out.println("Please confirm your details: ");
        System.out.println("\tName: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("\tEmail: " + customer.getEmail());

        System.out.println("\nWhat would you like to do?");
        System.out.println("\t1. Continue to next");
        System.out.println("\t2. Edit order");
        System.out.println("\t3. Go back to home");
        System.out.print("\nEnter your choice (1-3): ");

        while (true)
        {
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-3).");
                System.out.print("Enter your choice (1-3): ");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)
            {
                // go to Item List
                showItemList();
                return;
            }
            else if (choice == 2)
            {
                showEditOrder();
                return;
            }
            else if (choice == 3)
            {
                // back to home
                return;
            }
            else
            {
                showErrorScreen("Please enter a number (1-3).");
                System.out.print("Enter your choice (1-3): ");
            }
        }
    }

    public void showItemList() {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Order Summary - Item List");

        System.out.println("Please confirm your order details: ");

        // AI-generated formatting
        String line = "-----------------------------------------------------------------------------------------------------";
        System.out.println(line);
        System.out.printf("| %-3s | %-28s | %-12s | %-12s | %-6s | %-12s |\n",
                "No.", "Product Name", "Price ($)", "Member ($)", "Qty", "Total ($)");
        System.out.println(line);

        int idx = 1;
        for (CartItem item : order.getOrderItems()) {
            String name = item.getProduct().getName();
            double price = item.getProduct().getPrice();
            double member = item.getProduct().getMemberPrice();
            int qty = item.getQuantity();
            double total = orderSummaryController.calculateLineTotal(item, customer);

            if (name.length() > 28) {
                name = name.substring(0, 25) + "...";
            }

            System.out.printf("| %3d | %-28s | %12.2f | %12.2f | %6d | %12.2f |\n",
                    idx, name, price, member, qty, total);
            idx++;
        }
        System.out.println(line);

        double subtotal = orderSummaryController.calculateSubtotal(order, customer);
        System.out.printf("%nSubtotal (regular prices): $%.2f%n", subtotal);

        System.out.println("\nWhat would you like to do?");
        System.out.println("\t1. Continue to next");
        System.out.println("\t2. Go back");
        System.out.println("\t3. Edit order");
        System.out.println("\t4. Go back to home");
        System.out.print("\nEnter your choice (1-4): ");

        while (true)
        {
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-4).");

                System.out.print("Enter your choice (1-4): ");

                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)
            {
                showDeliveryPickupInfo();
                return;
            }
            else if (choice == 2)
            {
                showCustomerDetails();
                return;
            }
            else if (choice == 3)
            {
                showEditOrder();
                return;
            }
            else if (choice == 4)
            {
                return; // go back to home
            }
            else
            {
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
            }
        }
    }

    public void showDeliveryPickupInfo()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Order Summary - Delivery/Pickup Information");

        System.out.println("Please confirm your order details:");

        if (order.getPickupDeliveryChoice() instanceof Pickup)
        {
            PickupStore pickupStore = pickupController.getPickupStore();

            if (pickupStore == null)
            {
                showErrorScreen("Pickup Store is invalid.");
                return;
            }

            System.out.println("\tCollection method: Pickup");
            System.out.println("\tStore details:");
            System.out.println("\t\t" + pickupStore.getName());
            System.out.println("\t\t" + pickupStore.getAddress());
            System.out.println("\t\tPhone: " + pickupStore.getPhone());
            System.out.println("\t\tBusiness hours: " + pickupStore.getBusinessHrs());
        }
        else if (order.getPickupDeliveryChoice() instanceof Delivery delivery)
        {
            Address address = delivery.getDeliveryAddress();

            if (address == null)
            {
                showErrorScreen("Delivery address is missing.");
                return;
            }

            System.out.println("\tCollection method: Delivery");
            System.out.println("\tDelivery address: " + address);

        }
        else
        {
            showErrorScreen("No Delivery or Pickup choice record found.");
            return;
        }

        System.out.println("\nWhat would you like to do?");
        System.out.println("\t1. Continue to next");
        System.out.println("\t2. Go back");
        System.out.println("\t3. Edit order");
        System.out.println("\t4. Go back to home");
        System.out.print("\nEnter your choice (1-4): ");

        while (true)
        {
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1)
            {
                showDiscounts();    // Continue to next (Discounts)
                return;
            }
            else if (option == 2)
            {
                showItemList();     // Go back
                return;
            }
            else if (option == 3)
            {
                showEditOrder();
                return;
            }
            else if (option == 4)
            {
                return;             // Go home
            }
            else
            {
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
            }
        }
    }

    public void showDiscounts()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Order Summary - Discounts");

        double subtotal       = orderSummaryController.calculateSubtotal(order, customer);
        double promoDiscount  = orderSummaryController.calculatePromoDiscount(subtotal, order);
        double pickupDiscount = orderSummaryController.calculateStudentPickupDiscount(subtotal, customer, order);

        // chooses the one that gives a cheaper discount
        double appliedDiscount = 0.00;
        String appliedDiscountString = null;

        if (promoDiscount > 0.00 && pickupDiscount > 0.00)
        {
            if (promoDiscount < pickupDiscount)
            {
                appliedDiscount = promoDiscount;
                appliedDiscountString = "Promo code";
            }
            else
            {
                appliedDiscount = pickupDiscount;
                appliedDiscountString = "Student pickup discount (5% off)";
            }
        }
        else if (promoDiscount > 0.00)
        {
            appliedDiscount = promoDiscount;
            appliedDiscountString = "Promo code";
        }
        else if (pickupDiscount > 0.00)
        {
            appliedDiscount = pickupDiscount;
            appliedDiscountString = "Student pickup discount (5% off)";
        }

        if (appliedDiscount > 0.00)
        {
            System.out.printf("\t%s: -$%.2f%n", appliedDiscountString, appliedDiscount);
        }
        else
        {
            System.out.println("\t(No discounts applied.)");
        }

        System.out.println("\nWhat would you like to do?");
        System.out.println("\t1. Continue to next");
        System.out.println("\t2. Go back");
        System.out.println("\t3. Edit order");
        System.out.println("\t4. Go back to home");
        System.out.print("\nEnter your choice (1-4): ");

        while (true)
        {
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1)
            {
                showOrderTotal();
                return;
            }
            else if (option == 2)
            {
                showDeliveryPickupInfo();   // Go back (by one screen)
                return;
            }
            else if (option == 3)
            {
                showEditOrder();
                return;
            }
            else if (option == 4)
            {
                return;                     // Go home
            }
            else
            {
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
            }
        }
    }

    public void showOrderTotal() {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Order Summary - Order Total");

        double subtotal       = orderSummaryController.calculateSubtotal(order, customer);
        double promoDiscount  = orderSummaryController.calculatePromoDiscount(subtotal, order);
        double pickupDiscount = orderSummaryController.calculateStudentPickupDiscount(subtotal, customer, order);

        // chooses the one that gives a cheaper discount
        double appliedDiscount = 0.00;
        String appliedDiscountString = null;

        if (promoDiscount > 0.00 && pickupDiscount > 0.00)
        {
            if (promoDiscount < pickupDiscount)
            {
                appliedDiscount = promoDiscount;
                appliedDiscountString = "Promo code";
            }
            else
            {
                appliedDiscount = pickupDiscount;
                appliedDiscountString = "Student pickup discount (5% off)";
            }
        }
        else if (promoDiscount > 0.00)
        {
            appliedDiscount = promoDiscount;
            appliedDiscountString = "Promo code";
        }
        else if (pickupDiscount > 0.00)
        {
            appliedDiscount = pickupDiscount;
            appliedDiscountString = "Student pickup discount (5% off)";
        }

        double deliveryFee = orderSummaryController.calculateDeliveryFee(order);
        double totalPayable = orderSummaryController.calculateTotalPayable(order, customer);

        boolean isDelivery = order.getPickupDeliveryChoice() instanceof Delivery;
        boolean isStudent = customer != null && customer.isStudent();

        order.setSubtotal(subtotal);
        order.setDiscountTotal(appliedDiscount);
        order.setTotalPayable(totalPayable);

        System.out.printf("\tSubtotal: $%.2f%n", subtotal);

        if (appliedDiscount > 0)
        {
            System.out.printf("\t%s: -$%.2f%n", appliedDiscountString, appliedDiscount);
        }

        if (isDelivery)
        {
            if (isStudent)
            {
                System.out.print("\tDelivery fee: $0.00 (Delivery fee waived for students)\n");
            }
            else
            {
                System.out.printf("\tDelivery fee: $%.2f%n", deliveryFee);
            }
        }

        System.out.printf("%n\tTotal payable: $%.2f%n%n", totalPayable);

        System.out.println("What would you like to do?");
        System.out.println("\t1. Place order (Order total will be deducted from account balance)");
        System.out.println("\t2. Go back");
        System.out.println("\t3. Edit order");
        System.out.println("\t4. Go back to home");
        System.out.print("\nEnter your choice (1-4): ");

        while (true)
        {
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)
            {
                placeOrder();
                return;
            }
            else if (choice == 2)
            {
                showDiscounts();
                return;
            }
            else if (choice == 3)
            {
                showEditOrder();
                return;
            }
            else if (choice == 4)
            {
                return; // Go home
            }
            else
            {
                showErrorScreen("Please enter a number (1-4).");
                System.out.print("Enter your choice (1-4): ");
            }
        }
    }

    public void showEditOrder()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Edit Order");

        System.out.println("\nWhich would you like to edit?");
        System.out.println("\t1. Edit Cart");
        System.out.println("\t2. Edit Delivery/Pickup Information");
        System.out.println("\t3. Edit Promo Code");
        System.out.println("\t4. Go back to Order Summary");
        System.out.print("\nEnter your choice (1-4): ");

        if (!scanner.hasNextInt())
        {
            scanner.nextLine();
            showErrorScreen("Please enter a number (1-4).");
            return;
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1)
        {
            // Edit Cart
            if (customer.getCart() == null)
            {
                showErrorScreen("Cart not found for this customer.");
                return;
            }

            CartController cartController = new CartController(customer.getCart());
            new CartView(cartController, null).showCart();

            order.setOrderItems(new java.util.ArrayList<>(customer.getCart().getCartItems()));

            // When CartView returns it comes back to Item List order summary
            System.out.print("\nPress Enter to return to Order Summary...");
            scanner.nextLine();
            showItemList();
        }
        else if (choice == 2)
        {
            // Edit Delivery/Pickup
            DeliveryPickupView deliveryPickupView = new DeliveryPickupView(customer, pickupController, order);
            String actionType = deliveryPickupView.showDeliveryPickupOptions();

            if (!actionType.equals("BACK"))
            {
                showDeliveryPickupInfo();
            }
        }
        else if (choice == 3)
        {
            // Edit Promo Code

            boolean isPickup = order.getPickupDeliveryChoice() instanceof Pickup;

            if (promoController == null)
            {
                showErrorScreen("Promo feature is unavailable right now.");
                return;
            }

            boolean proceed = new PromoView(promoController, customer, order, isPickup)
                    .showPromoCodeScreen();

            // Shows discounts again when it returns
            if (proceed)
            {
                showDiscounts();
            }
        }
        else if (choice == 4)
        {
            // Returns to whatever summary screen called this
        }
        else
        {
            showErrorScreen("Please enter a number (1-4).");
        }
    }

    private void placeOrder()
    {
        try
        {
            boolean successful = orderSummaryController.placeOrder(customer, order, "data/Customer.txt", "data/Products.txt"
            );

            if (!successful)
            {
                // If insufficient funds or file error
                showErrorScreen("Could not place order: Insufficient Funds");
                return;
            }

            // otherwise successful

            if (customer != null && customer.getCart() != null)
            {

                // saving the order details to look back to.
                orderSummaryController.saveOrderInfo(customer, order);

                customer.getCart()
                        .clearCart();
            }

            order.setConfirmed(true);

            System.out.println("\nOrder placed! Press Enter to continue...");
            scanner.nextLine();

            new OrderConfirmationView(pickupController, customer, order)
                    .showOrderConfirmationScreen();
        }
        catch (IOException e)
        {
            showErrorScreen(e.getMessage());
        }
    }
}