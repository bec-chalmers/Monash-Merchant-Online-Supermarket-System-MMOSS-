package boundry;

import controllers.*;
import entity.*;
import controllers.PickupController;
import controllers.ProductController;
import controllers.FindProductsController;
import controllers.CartController;
import controllers.PromoController;
import controllers.ProfileController;
import entity.Order;
import entity.User;
import entity.Customer;
import util.BannerUtil;

import java.util.ArrayList;
import java.util.Scanner;

import static util.ErrorUtil.showErrorScreen;

public class CustomerView {

    private final User loggedInUser;
    private final PickupController pickupController;
    private final PromoController promoController;
    private final CartController cartController;
    private final ProductController productController;
    private final FindProductsView findProductsView;

    public CustomerView(User user,
                        ProductController productController,
                        PickupController pickupController,
                        PromoController promoController) {
        this.loggedInUser = user;
        this.pickupController = pickupController;
        this.promoController = promoController;
        this.productController = productController;

        Customer customer = (Customer) user;
        if (customer.getCart() == null) {
            customer.setCart(new Cart());
        }

        this.cartController = new CartController(customer.getCart());

        FindProductsController findProductsController = new FindProductsController(productController);
        this.findProductsView = new FindProductsView(findProductsController, cartController, this);
    }

    public void showCustomerMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        do {
            BannerUtil.printHeader();
            BannerUtil.printSubheader("Welcome - " + loggedInUser.getFirstName()); // to display the name of the user
            System.out.println("1. Browse Products");
            System.out.println("2. View Profile");
            System.out.println("3. Add Funds");
            System.out.println("4. Purchase VIP Membership");
            System.out.println("5. Exit MMOSS");
            System.out.print("Enter your choice (1-5): ");

            // Input validation
            if (!scanner.hasNextInt()) {
                scanner.nextLine(); // ignores non-int
                showErrorScreen("Please enter a number.");
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            // validates that user enters a number between 1-5
            if (choice < 1 || choice > 5) {
                showErrorScreen("Please enter a number between 1 and 5.");
                continue;
            }

            // these are temporary placeholders-
            // put the function call inside accordingly
            switch (choice) {
                case 1:
                    System.out.println("Browse Products --");
                    findProductsView.defaultBrowsing();
                    break;
                case 2:
                    ProfileController profileController = new ProfileController((Customer) loggedInUser);
                    ProfileView profileView = new ProfileView(profileController);
                    profileView.displayProfile();
                    break;
                case 3:
                    System.out.println("Add Funds ---");
                    new AddFundsView().displayAddFundsMenu((Customer) loggedInUser);
                    break;

                case 4:
                    System.out.println("Purchase VIP Membership");
                    VIPMembershipView vip = new VIPMembershipView((Customer) loggedInUser);
                    vip.vipMenu();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);
    }

    protected User getLoggedInUser() {
        return loggedInUser;
    }

    public void beginCheckout() {
        int orderNo = loadNextOrderNumber();
        Order order = new Order(orderNo);
        order.setOrderItems(new ArrayList<>(cartController.getCart().getCartItems()));

        while (true) {
            String choice = new DeliveryPickupView((Customer) loggedInUser, pickupController, order)
                    .showDeliveryPickupOptions();

            if (choice.equals("BACK")) {
                return;
            }

            boolean isPickup = order.getPickupDeliveryChoice() instanceof Pickup;
            boolean proceed = new PromoView(promoController, (Customer) loggedInUser, order, isPickup)
                    .showPromoCodeScreen();

            if (proceed) {
                new OrderSummaryView(new OrderSummaryController(productController), pickupController, promoController, (Customer) loggedInUser, order).showOrderSummary();

                return;
            }
        }
    }

    // Reads the current order number from CSV, increments it, and writes over the next order number to the CSV file
    private int loadNextOrderNumber() {
        String filePath = "data/OrderNumber.txt";
        int currentOrderNo = 1000;

        try {
            java.util.List<String> lines = util.FileStorageUtil.readSingleColumnFile(filePath);

            if (!lines.isEmpty()) {
                currentOrderNo = Integer.parseInt(lines.getFirst().trim());
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to load next order number. Starting from 1000.");
        }

        int nextOrderNo = currentOrderNo + 1;

        // writes new order number back to CSV
        try {
            java.util.List<String> fileContents = new java.util.ArrayList<>();
            fileContents.add("next_order_no");
            fileContents.add(String.valueOf(nextOrderNo));
            util.FileStorageUtil.writeToFile(filePath, fileContents);
        } catch (Exception ignored) {
            // counter will stop when write errors are encountered
        }

        return nextOrderNo;
    }
}
