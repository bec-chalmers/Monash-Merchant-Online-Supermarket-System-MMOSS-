package controllers;

import entity.Customer;
import entity.VIPMembership;
import util.FileStorageUtil;
import java.time.LocalDate;
import java.io.*;
import java.util.*;

/** A controller class which deals with
 * VIP Membership operations (purchase, renew, cancel)
 * logic for customer.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class VIPMembershipController {
    private final Customer customer;
    private final List<VIPMembership> membershipHistory;
    private static final String VIP_TXT = "data/VIPMemberships.txt";

    /**
     * Non-Default Constructor for VIPMembershipController class.
     *
     * @param   customer    customer object
     */
    public VIPMembershipController(Customer customer) {
        this.customer = customer;
        this.membershipHistory = loadMembershipsFromFile();
        // Ensure customer object stays in sync
        customer.setMembershipHistory(new ArrayList<>(membershipHistory));
    }

    /**
     * Method for purchasing new vip membership by customers.
     *
     * @param years The number of years integer that the
     *              customer wants to purchase the membership
     *              for.
     *
     */
    public void purchaseNewVip(int years) throws Exception {
        //check for number of years entered
        checkYears(years);

        double membershipCost = years * VIPMembership.getVipCostPerYear(); //calculating total price
        // check for balance
        checkFundsForMembership(membershipCost);

        VIPMembership vip = new VIPMembership(years, "Purchase");
        // if all checks passed, buy the membership
        customer.setBalance(customer.getBalance() - membershipCost);
        membershipHistory.add(vip);
        saveMembershipToFile(vip);
        customer.setMembershipHistory(new ArrayList<>(membershipHistory));
    }

    /**
     * Method for renewing the current vip membership by customers.
     *
     * @param years The number of years integer that the
     *              customer wants to renew the membership
     *              for.
     *
     */
    public void renewVIP(int years) throws IllegalArgumentException, Exception {
        //check for number of years entered
        checkYears(years);
        // finding all active memberships
        VIPMembership activeMembership = getActiveVIP();
        // check if there is no active membership to renew
        if (activeMembership == null) {
            throw new Exception("There are no active VIP memberships currently for you to renew.");
        }
        double membershipCost = years * VIPMembership.getVipCostPerYear(); //calculating total price
        // check for balance
        checkFundsForMembership(membershipCost);

        // if all checks passed, membership is renewed
        customer.setBalance(customer.getBalance() - membershipCost);
        //extending the expiry date of the current memberhsip which got renewed
        activeMembership.extendExpiry(years);
        //recording membership history
        VIPMembership renewalRecord = new VIPMembership(years, "Renewal");

        membershipHistory.add(renewalRecord);
        customer.getMembershipHistory().add(renewalRecord);

        saveMembershipToFile(renewalRecord);
    }

    /**
     * Method for finding the active VIP memberships
     * for a particular customer.
     *
     * @return VIPMembership   Returns the VIPMembership object
     * which is active.
     *
     */
    private VIPMembership getActiveVIP() {
        for (int i = membershipHistory.size() - 1; i >= 0; i--)
        {
            if (membershipHistory.get(i).isActive())
                return membershipHistory.get(i);
        }
        // if no active membership found, we return null
        return null;
    }

    /**
     * Method for cancelling the active VIP membership
     * for a particular customer.
     *
     */
    public void cancelVIP() throws Exception {
        // find the active membership to cancel
        VIPMembership activeMembership = getActiveVIP();

        // check if there is no active membership to renew
        if (activeMembership == null) {
            throw new Exception("There are no active VIP memberships currently for you to cancel.");
        }

        // if there is active membership, we cancel here
        activeMembership.cancelMembership(LocalDate.now());
        // adding to history

        VIPMembership cancelRecord = new VIPMembership(activeMembership.getType(), activeMembership.getStartDate(),
                activeMembership.getExpiryDate(), false);
        membershipHistory.add(cancelRecord);
        customer.getMembershipHistory().add(cancelRecord);

        saveMembershipToFile(cancelRecord);
    }

    /**
     * Method for checking if the customer balance is
     * sufficient for purchasing the membership.
     *
     * @param membershipCost   The membership cost as double.
     *
     */
    private void checkFundsForMembership(double membershipCost) throws Exception {
        if (customer.getBalance() < membershipCost) {
            throw new Exception("Insufficient balance to buy/renew VIP membership.");
        }
    }

    /**
     * Method for checking if year entered for membership is
     * valid
     *
     * @param years   The membership years as integer.
     */
    private void checkYears(int years) {
        if (years <= 0)
            throw new IllegalArgumentException("Invalid number of years of membership.");
    }

    /**
     * Accessor method for getting the membership history.
     * This method code is AI generated.
     *
     * @return List<VIPMembership>  The List of VIPMembership object.
     */
    public List<VIPMembership> getMembershipHistory() {
        return Collections.unmodifiableList(membershipHistory);
    }

    /**
     * Accessor method for getting the membership history.
     *
     * @return List<VIPMembership>  The List of VIPMembership object.
     */
    public boolean hasActiveVIP() {
        return getActiveVIP() != null; // true if active exists.
    }

    /**
     * Method which saves the membership details and any updated of
     * membership status of a customer to a file to store
     * the membership history even after logging out.
     *
     * @param vipMembership  The VIPMembership object
     */
    private void saveMembershipToFile(VIPMembership vipMembership) throws IOException {
        File file = new File(VIP_TXT);
        boolean isNewFile = !file.exists() || file.length() == 0;

        // Prepare the CSV row
        String record = String.join(",",
                customer.getEmail().trim(),
                vipMembership.getType().trim(),
                vipMembership.getStartDate().toString(),
                vipMembership.getExpiryDate().toString(),
                vipMembership.isActive() ? "Active" : "Inactive"
        );

        // If new file, write header first
        if (isNewFile)
        {
            List<String> lines = new ArrayList<>();
            lines.add("Email,Type,StartDate,ExpiryDate,Status"); // heading information
            lines.add(record);
            FileStorageUtil.writeToFile(VIP_TXT, lines);
        }
        else
        {
            // Append record to existing file
            FileStorageUtil.appendToFile(VIP_TXT, record);
        }
    }

    /**
     * Method which loads the membership details from the
     * file and adds to an arraylist of VIPMembership objects
     * to use it after user logs in.
     *
     * @return ArrayList<VIPMembership>  ArrayList of VIPMembership object
     */
    public ArrayList<VIPMembership> loadMembershipsFromFile()
    {
        ArrayList<VIPMembership> history = new ArrayList<>();

        try
        {
            // Use FileStorageUtil to read the CSV (skips the header internally)
            List<List<String>> rawData = FileStorageUtil.readMultiColumnFile(VIP_TXT);

            for (List<String> row : rawData)
            {
                if (row.size() < 5) continue;

                String email = row.get(0).trim();
                if (!email.equalsIgnoreCase(customer.getEmail()))
                    continue;

                String type = row.get(1).trim();
                LocalDate startDate = LocalDate.parse(row.get(2).trim());
                LocalDate expiryDate = LocalDate.parse(row.get(3).trim());
                boolean isActive = row.get(4).trim().equalsIgnoreCase("Active");

                history.add(new VIPMembership(type, startDate, expiryDate, isActive));
            }
        }
        catch (IOException e)
        {
            System.out.println("Error loading membership data: " + e.getMessage());
        }
        return history;
    }
}

