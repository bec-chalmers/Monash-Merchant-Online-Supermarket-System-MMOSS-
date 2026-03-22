package boundry;
import controllers.ProfileController;
import util.BannerUtil;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** A boundary class which deals with
 * Profile operations (profile details, viewing
 * membership purchase history and order history)
 * user interaction and view.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class ProfileView
{

    private final ProfileController controller;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Non-Default Constructor for ProfileView class.
     *
     * @param   controller    ProfileController object
     */
    public ProfileView(ProfileController controller)
    {
        this.controller = controller;
    }

    /**
     * Method which is used for displaying the profile
     * details of a customer and shows the next options for
     * profile history menu.
     *
     */
    public void displayProfile()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Profile Details");
        controller.getProfileDetails().forEach(System.out::println);

        int choice = -1;
        do {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View Membership History");
            System.out.println("2. View Order History");
            System.out.println("3. Return to Home");
            System.out.print("Enter choice (1-2): ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine(); // consume invalid input
                System.out.println("Please enter a valid number.");
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    displayMembershipHistory();
                    break;
                case 2:
                    displayOrderHistory();
                case 3:
                    System.out.println("Returning to Home...");
                    break;
                default:
                    System.out.println("Please enter a number between 1 and 2.");
            }
        } while (choice != 3);
    }

    /**
     * Displays the logged-in customer's detailed order history.
     * Shows 20 records per page, formatted with all CSV attributes.
     */
    private void displayOrderHistory()
    {
        try
        {
            List<String> allOrders = controller.getCustomerOrderHistory();

            if (allOrders.isEmpty())
            {
                BannerUtil.printHeader();
                BannerUtil.printSubheader("View Order History");
                System.out.println("No order history found for this customer.");
                return;
            }

            final int PAGE_SIZE = 20;
            int currentPage = 0;

            while (true)
            {
                int start = currentPage * PAGE_SIZE;
                int end = Math.min(start + PAGE_SIZE, allOrders.size());
                List<String> currentPageOrders = allOrders.subList(start, end);

                BannerUtil.printHeader();
                BannerUtil.printSubheader("View Order History");

                System.out.println("Showing previous 20 records:\n");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-4s %-25s %-15s %-22s %-12s %-12s %-10s %-15s %-20s%n",
                        "#", "Customer Email", "Membership", "Order Date", "Pickup/Delv", "Promo Code", "Discount", "Total ($)", "Item Summary");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                for (int i = 0; i < currentPageOrders.size(); i++)
                {
                    String[] cols = currentPageOrders.get(i).split(",\\s*");
                    // Defensive indexing
                    String email = (cols.length > 0) ? cols[0] : "N/A";
                    String membership = (cols.length > 1 && !cols[1].isEmpty()) ? cols[1] : "-";
                    String orderDateTime = (cols.length > 2) ? cols[2] : "N/A";
                    String pickupType = (cols.length > 3) ? cols[3] : "-";
                    String promo = (cols.length > 4 && !cols[4].isEmpty()) ? cols[4] : "-";
                    String discount = (cols.length > 5) ? cols[5] : "0.0";
                    String total = (cols.length > 6) ? cols[6] : "0.0";
                    String items = (cols.length > 7) ? cols[7] : "-";

                    // Split date & time for clarity
                    String datePart = orderDateTime.contains("T") ? orderDateTime.split("T")[0] : orderDateTime;
                    String timePart = orderDateTime.contains("T") ? orderDateTime.split("T")[1].split("\\.")[0] : "";

                    System.out.printf("%-4d %-25s %-15s %-22s %-12s %-12s %-10s %-15s %-20s%n",
                            (i + 1),
                            email,
                            membership,
                            datePart + "  " + timePart,
                            pickupType,
                            promo,
                            discount,
                            total,
                            items);
                }

                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");


                boolean hasNextPage = end < allOrders.size();

                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Select an order record");
                if (hasNextPage)
                {
                    System.out.println("2. Next page");
                    System.out.println("3. Go back");
                }
                else
                {
                    System.out.println("2. Go back");
                }

                System.out.print("\nEnter your choice: ");
                String input = scanner.nextLine().trim();

                if (hasNextPage)
                {
                    switch (input)
                    {
                        case "1":
                            System.out.print("Enter the record number to view details: ");
                            String selected = scanner.nextLine();
                            System.out.println("\nViewing detailed info for Order #" + selected + " (not implemented yet).");
                            System.out.println("Press Enter to return...");
                            scanner.nextLine();
                            break;
                        case "2":
                            currentPage++;
                            break;
                        case "3":
                            return;
                        default:
                            System.out.println("Invalid choice. Please select 1, 2, or 3.");
                            break;
                    }
                }
                else
                {
                    switch (input)
                    {
                        case "1":
                            System.out.print("Enter the record number to view details: ");
                            String selected = scanner.nextLine();
                            try {
                                int recordIndex = Integer.parseInt(selected) - 1;
                                if (recordIndex >= 0 && recordIndex < currentPageOrders.size()) {
                                    String orderLine = currentPageOrders.get(recordIndex);
                                    displaySingleOrderDetails(orderLine);
                                } else {
                                    System.out.println("Invalid record number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number.");
                            }
                            break;
                        case "2":
                            return;
                        default:
                            System.out.println("Invalid choice. Please select 1 or 2.");
                            break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error occurred while reading file.");
        }
    }


    /**
     * Displays a detailed view of a single order record.
     * This view is shown after the user selects an order from the order history list.
     *
     * @param orderLine the CSV-formatted string representing a single order record
     */
    private void displaySingleOrderDetails(String orderLine)
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("View Order History");

        if (orderLine == null || orderLine.isEmpty()) {
            System.out.println("No order data available for this record.");
            System.out.println("Press Enter to return...");
            scanner.nextLine();
            return;
        }

        // Split by comma, accounting for possible empty cells
        String[] cols = orderLine.split(",\\s*");

        // Defensive parsing
        String email = (cols.length > 0) ? cols[0] : "-";
        String membership = (cols.length > 1 && !cols[1].isEmpty()) ? cols[1] : "-";
        String orderDateTime = (cols.length > 2) ? cols[2] : "-";
        String pickupType = (cols.length > 3) ? cols[3] : "-";
        String promo = (cols.length > 4 && !cols[4].isEmpty()) ? cols[4] : "None";
        String discount = (cols.length > 5) ? cols[5] : "0.0";
        String total = (cols.length > 6) ? cols[6] : "0.0";
        String itemSummary = (cols.length > 7) ? cols[7] : "-";

        // Split date & time for readability
        String datePart = orderDateTime.contains("T") ? orderDateTime.split("T")[0] : orderDateTime;
        String timePart = orderDateTime.contains("T") ? orderDateTime.split("T")[1].split("\\.")[0] : "";

        // Print main order info
        System.out.printf("Order for %s (%s)%n", email, membership.equals("-") ? "Standard" : membership);
        System.out.printf("Date: %s %s%n", datePart, timePart);
        System.out.println("---------------------------------------------------");
        System.out.println("Item List:");
        System.out.println("---------------------------------------------------");
        System.out.printf("| %-3s | %-25s | %-10s | %-5s | %-10s |%n",
                "No", "Product Name", "Price($)", "Qty", "Total($)");
        System.out.println("---------------------------------------------------");

        // Parse item summary (new: "Name:Price:Qty:Total", old: "Name:Qty")
        String[] items = itemSummary.split(";");
        int counter = 1;

        for (String item : items)
        {
            if (item.trim().isEmpty()) continue;

            String[] parts = item.split(":");
            String name = parts[0].trim();
            String price = "-";
            String qty = "-";
            String totalItem = "-";

            // Support both old and new formats
            if (parts.length == 2) {
                // old format: product:qty
                qty = parts[1].trim();
            }
            else if (parts.length >= 4) {
                // new format: product:unitPrice:qty:total
                price = formatCurrency(parts[1].trim());
                qty = parts[2].trim();
                totalItem = formatCurrency(parts[3].trim());
            }

            System.out.printf("| %-3d | %-25s | %-10s | %-5s | %-10s |%n",
                    counter++, name, price, qty, totalItem);
        }

        System.out.println("---------------------------------------------------");
        System.out.printf("Order type: %s%n", pickupType);
        System.out.println("Discounts:");
        System.out.printf("  Promo code: %s%n", promo);
        System.out.printf("  Discount total: AUD $%.2f%n", Double.parseDouble(discount));
        System.out.printf("Order total: AUD $%.2f (including delivery)%n", Double.parseDouble(total));
        System.out.println("---------------------------------------------------");
        System.out.println("Press Enter to return to Order History...");
        scanner.nextLine();
    }

    /**
     * Method which displays the membership history details
     * to user.
     *
     */
    public void displayMembershipHistory() {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Membership Purchase History");

        List<String> history = controller.getMembershipHistoryDetails();
        if (history.isEmpty()) {
            System.out.println("No membership history found.");
        } else {
            for (String record : history) {
                System.out.println(record);
                System.out.println("-----------------------------");
            }
        }

        System.out.println("Press Enter to return...");
        scanner.nextLine(); // pause before returning to profile menu
    }

    /**
     * Formats a numeric string into two-decimal currency form.
     * Handles invalid or blank inputs gracefully.
     */
    private String formatCurrency(String value)
    {
        try {
            double num = Double.parseDouble(value);
            return String.format("%.2f", num);
        } catch (NumberFormatException e) {
            return "-";
        }
    }
}
