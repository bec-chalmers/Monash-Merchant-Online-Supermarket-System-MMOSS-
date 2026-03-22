package controllers;

import entity.Customer;
import entity.VIPMembership;
import util.FileStorageUtil;
import java.io.IOException;
import java.util.*;

/** A controller class which deals with
 * Profile operations (profile details, viewing
 * membership purchase history and order history)
 * logic for customer.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class ProfileController {

    private final Customer customer;
    private final VIPMembershipController membershipController;

    /**
     * Non-Default Constructor for ProfileController class.
     *
     * @param   customer    customer object
     */
    public ProfileController(Customer customer)
    {
        this.customer = customer;
        this.membershipController = new VIPMembershipController(customer);
    }

    /**
     * Method which is used for returning the profile details of
     * a particular customer.
     *
     * @return   ArrayList<String>    ArrayList of profile details as string
     */
    public ArrayList<String> getProfileDetails() {
        ArrayList<String> details = new ArrayList<>();

        boolean VIPstatus = membershipController.hasActiveVIP(); // use controller

        details.add("Name: " + customer.getFirstName() + " " + customer.getLastName());
        details.add("Gender: " + customer.getGender());
        details.add("DOB: " + customer.getDateOfBirth());
        details.add("Mobile: " + customer.getMobileNumber());
        details.add("Address: " + customer.getAddress());
        details.add("VIP Status: " + (VIPstatus ? "Active" : "Inactive"));
        details.add(String.format("Balance: $%,.2f", customer.getBalance()));

        return details;
    }

    /**
     * Gets and merges the order history of the current logged-in customer.
     * Merging is done per day AND per membership type (Regular/VIPMember).
     * Each merged record combines items, discounts, and totals.
     *
     * @return List<String> merged order records as CSV-style strings.
     */
    public List<String> getCustomerOrderHistory() throws IOException
    {
        List<List<String>> history = FileStorageUtil.readMultiColumnFile("data/OrderHistory.txt");
        String currentEmail = customer.getEmail();

        // Use LinkedHashMap to preserve order of insertion
        Map<String, List<String>> mergedOrders = new LinkedHashMap<>();

        for (List<String> line : history)
        {
            if (line.isEmpty() || line.getFirst().equalsIgnoreCase("user_name")) continue; // skip header

            String email = line.get(0).trim();
            if (!email.equalsIgnoreCase(currentEmail)) continue;

            // Defensive field reads
            String membership = (line.size() > 1 && !line.get(1).trim().isEmpty()) ? line.get(1).trim() : "-";
            String orderDateTime = (line.size() > 2) ? line.get(2).trim() : "";
            String pickupType = (line.size() > 3) ? line.get(3).trim() : "-";
            String promoCode = (line.size() > 4) ? line.get(4).trim() : "-";
            String discount = (line.size() > 5) ? line.get(5).trim() : "0.0";
            String total = (line.size() > 6) ? line.get(6).trim() : "0.0";
            String itemSummary = (line.size() > 7) ? line.get(7).trim() : "-";

            // Extract date part only for grouping (YYYY-MM-DD)
            String datePart = orderDateTime.contains("T")
                    ? orderDateTime.split("T")[0]
                    : orderDateTime;

            // Create grouping key: email + date + membership type
            String key = email + "_" + datePart + "_" + membership;

            if (mergedOrders.containsKey(key))
            {
                List<String> existing = mergedOrders.get(key);

                // Merge item summaries
                String existingItems = existing.get(7);
                existing.set(7, existingItems + ";" + itemSummary);

                // Merge totals
                double oldTotal = Double.parseDouble(existing.get(6));
                double newTotal = Double.parseDouble(total);
                existing.set(6, String.format("%.2f", (oldTotal + newTotal)));

                // Merge discount totals
                double oldDiscount = Double.parseDouble(existing.get(5));
                double newDiscount = Double.parseDouble(discount);
                existing.set(5, String.format("%.2f", (oldDiscount + newDiscount)));

                // If promo code was empty before but exists now, update it
                if ((existing.get(4).equals("-") || existing.get(4).isEmpty()) && !promoCode.equals("-"))
                    existing.set(4, promoCode);

                // Always keep latest date/time for display
                existing.set(2, orderDateTime);
            }
            else
            {
                // Make a copy so we don’t mutate original
                mergedOrders.put(key, new ArrayList<>(List.of(
                        email, membership, orderDateTime, pickupType,
                        promoCode, discount, total, itemSummary
                )));
            }
        }

        // Convert map values to string list
        List<String> mergedHistory = new ArrayList<>();
        for (List<String> row : mergedOrders.values())
        {
            mergedHistory.add(String.join(", ", row));
        }

        return mergedHistory;
    }

    /**
     * Method which takes all the membership history details and
     * returns it as a string to be viewed as history.
     *
     * @return List<String> history of membership as a string list.
     */
    public List<String> getMembershipHistoryDetails() {
        List<String> historyDetails = new ArrayList<>();
        List<VIPMembership> memberships = membershipController.getMembershipHistory();

        if (memberships.isEmpty())
        {
            historyDetails.add("No VIP membership history found.");
        } else {
            for (VIPMembership vip : memberships) {
                historyDetails.add(String.format(
                        "Type: %s | Start: %s | Expiry: %s | Status: %s",
                        vip.getType(),
                        vip.getStartDate(),
                        vip.getExpiryDate(),
                        vip.isActive() ? "Active" : "Inactive"
                ));
            }
        }
        return historyDetails;
    }

}
