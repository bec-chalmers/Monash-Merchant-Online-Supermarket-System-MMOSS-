package controllers;

import entity.Customer;
import entity.Promotion;
import util.FileStorageUtil;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates promo code entered
 * against a list of valid promo codes
 *
 * @author Rebecca Chalmers
 * @version 1.4
 */
public class PromoController
{
    // List of valid promotions
    private final List<Promotion> promotions = new ArrayList<>();

    /**
     * Constructs a PromoController object and adds all available promotions
     * to the list of promotions.
     */
    public PromoController()
    {
        try
        {
            loadPromotionsFromFile("data/Promotions.txt");
        }
        catch (IOException e)
        {
            System.out.println("Unable to load promotions from file. " + e.getMessage());
        }
    }

    /**
     * Finds a promotion by its code
     *
     * @param code the code that will be used to search
     *             for a corresponding promotion object
     *
     * @return the corresponding promotion, else null
     */
    public Promotion findPromotionByCode(String code)
    {
        if (code == null)
        {
            return null;
        }

        // Ignores case and surrounding spaces
        String trimmedCode = code.trim().toUpperCase();

        for (Promotion promotion : promotions)
        {
            // Retrieves the code's corresponding promotion object (if available)
            if (promotion.getCode().equalsIgnoreCase(trimmedCode))
            {
                return promotion;
            }
        }

        return null;
    }

    /**
     * Loads promotions from a multi-column CSV file.
     *
     * @param path path to the promotions file
     * @throws IOException if the file cannot be read
     */
    public void loadPromotionsFromFile(String path) throws IOException
    {
        // Read rows from a multi-column file
        List<List<String>> rows = FileStorageUtil.readMultiColumnFile(path);

        for (List<String> row : rows)
        {
            // skips rows that are too short
            if (row == null || row.size() < 7)
            {
                continue;
            }

            String promoCode = row.get(0).trim();
            String description = row.get(1).trim();

            int amountOff = 0;
            int percentageOff = 0;

            try
            {
                amountOff = Integer.parseInt(row.get(2).trim());
            } catch (NumberFormatException ignored)
            {

            }

            try
            {
                percentageOff = Integer.parseInt(row.get(3).trim());
            } catch (NumberFormatException ignored)
            {

            }

            String valid = row.get(4).trim();
            String type = row.get(5).trim();
            String condition = row.get(6).trim();

            promotions.add(new Promotion(promoCode, description, amountOff, percentageOff, valid, type, condition));
        }
    }

    /**
     * Validates whether a promo code can be applied for the given customer and
     * order type (pickup vs delivery).
     *
     * @param code          the promo code string entered by the user
     * @param customer      the current customer, used to check first-order rule
     * @param isPickupOrder true if this order is pickup; false if delivery
     *
     * @return true if the promo code is valid and passes validation rules; otherwise false
     */
     public boolean validateCode(String code, Customer customer, boolean isPickupOrder)
     {
         // Finds the corresponding promotion record
        Promotion promotion = findPromotionByCode(code);

        if (promotion == null)
        {
            return false;
        }

        String type = promotion.getType();
        String conditions = promotion.getConditions();

        // Must be customer's first pickup order
        if (type.equalsIgnoreCase("FIRST_ORDER"))
        {
            boolean isFirstOrder = customer.getOrderHistory() == null || customer.getOrderHistory().isEmpty();
            boolean pickupOnly = conditions.equalsIgnoreCase("PICKUP_ONLY");

            // Valid if first order and pickup
            return isFirstOrder && (!pickupOnly || isPickupOrder);
        }

        // Valid only within a window of time
        if (type.equalsIgnoreCase("TIME_BASED"))
        {
            String[] parts = conditions.split("-");

            // Requires 2 parts (start-end)
            if (parts.length != 2)
            {
                return false;
            }

            // Takes local Melbourne time
            LocalTime start = LocalTime.parse(parts[0].trim());
            LocalTime end = LocalTime.parse(parts[1].trim());
            LocalTime now = ZonedDateTime.now(ZoneId.of("Australia/Melbourne")).toLocalTime();

            return !now.isBefore(start) && now.isBefore(end);
        }

        return false;
     }
}