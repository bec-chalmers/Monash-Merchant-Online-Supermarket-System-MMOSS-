package controllers;

import entity.*;
import util.FileStorageUtil;
import java.io.IOException;
import java.util.List;

/**
 * Controller class that loads a pickup store
 * from CSV into memory.
 *
 * @author Rebecca Chalmers
 * @version 1.1
 */
public class PickupController
{
    private final String path;
    private PickupStore pickupStore;

    /**
     * Non-Default Constructor for PickupController class.
     * Creates a controller and immediately attempts to load the store
     * from the CSV filepath.
     *
     * @param path file path to the pickup store CSV
     * @throws RuntimeException if the file cannot be read or parsed
     */
    public PickupController(String path)
    {
        this.path = path;
        try
        {
            loadStore();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load pickup store. ", e);
        }
    }

    /**
     * Loads the first row of the CSV at path and builds a PickupStore object.
     *
     * @throws IOException if there is an I/O error reading the file
     * @throws IllegalArgumentException if the file has no data rows or a row has too few columns
     */
    public void loadStore() throws IOException
    {
        // Reads a multi-column CSV
        List<List<String>> rows = FileStorageUtil.readMultiColumnFile(path);

        // Must have at least one row
        if (rows.isEmpty())
        {
            throw new IllegalArgumentException("Pickup store CSV has no data rows: " + path);
        }

        // Assumes first row is the pickup store
        List<String> row = rows.get(0);

        // Expects 9 rows for all PickupStore attributes
        if (row.size() < 9)
        {
            throw new IllegalArgumentException("Invalid pickup store record - row: " + row);
        }

        // Maps columns to fields
        String name            = row.get(0);
        String buildingNumber  = row.get(1);
        String streetNumber    = row.get(2);
        String streetName      = row.get(3);
        String suburb          = row.get(4);
        String state           = row.get(5);
        String postcode        = row.get(6);
        String phone           = row.get(7);
        String businessHrs     = row.get(8);

        // Builds Address object
        Address pickupAddress = new Address(buildingNumber, streetNumber, streetName, suburb, state, postcode);
        this.pickupStore = new PickupStore(name, phone, pickupAddress, businessHrs);
    }

    /**
     * Returns the loaded pickup store.
     *
     * @return the PickupStore, or null if loading failed before assignment
     */
    public PickupStore getPickupStore()
    {
        return pickupStore;
    }
}