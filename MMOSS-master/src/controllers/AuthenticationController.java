package controllers;
import entity.*;
import util.FileStorageUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all the registered (assumed for this case) users in to memory.
 * User credentials are being verified as the user logs in.
 *
 * @author Yapa Jayasinghe
 * @version 1.2
 */
public class AuthenticationController
{

    // At this point all users stored are customers
    private final List<User> customers;
    private final String pathToStoredUsers;

    /**
     * Constructs an AuthenticationController obj and immediately load the customer data.
     * @param path path to the Customer csv file.
     */
    public AuthenticationController(String path)
    {
        this.pathToStoredUsers = path;
        this.customers = new ArrayList<>();
        try {
            loadUsers();
        }catch (IOException e){
            throw new RuntimeException("Failed to load stored users. ", e);
        }
    }

    /**
     * Read the raw data in using the util class FileStorage,
     * Creates a list of User objects
     */
    public void loadUsers() throws IOException
    {
        customers.clear(); // to prevent appending of duplicate users(if).
        List<List<String>> rawData = FileStorageUtil.readMultiColumnFile(pathToStoredUsers);

        for (List<String> row : rawData)
        {
            if (row.size()<14)
            {
                throw new IllegalArgumentException("Invalid user record, row - "+row);
            }

            String email = row.get(0);
            String password = row.get(1);
            String firstName = row.get(2);
            String lastName = row.get(3);
            String mobile = row.get(4);
            String dateOfBirth = row.get(5);
            String gender = row.get(6);
            String buildingNumber = row.get(7);
            String streetNumber = row.get(8);
            String streetName = row.get(9);
            String suburb = row.get(10);
            String state = row.get(11);
            String postcode = row.get(12);
            boolean isStudent = Boolean.parseBoolean(row.get(13));
            double balance = Double.parseDouble(row.get(14));

            Address address = new Address(buildingNumber, streetNumber, streetName, suburb, state, postcode);

            Customer cObj = new Customer(
                    email, password, firstName, lastName, mobile,
                    dateOfBirth, gender, address,
                    isStudent, balance,
                    new ArrayList<>(), new ArrayList<>(), new Cart()
            );

            // added for loading VIP membership history from file for customer
            VIPMembershipController vipController = new VIPMembershipController(cObj);
            cObj.setMembershipHistory(vipController.loadMembershipsFromFile());

            customers.add(cObj);

        }
    }

    /**
     * Non admin user credentials are being checked against the stored data of the users.
     * The admin credential are being checked against the hardcoded credentials of the admin in memory.
     *
     * @param email the monash email address of a user.
     * @param password the relevant password of a user.
     * @return User object - if authentication succeeds, else null.
     */
    public User authenticateUser(String email, String password) {
        if (email == null || email.isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (!email.endsWith("@monash.edu")){
            throw new IllegalArgumentException("Invalid email (must be a Monash address).");
        }
        if (password == null || password.isEmpty()){
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // check for admin user
        String ADMIN_EMAIL = "admin@monash.edu";
        String ADMIN_PASSWORD = "Monash1234!";
        if (email.equalsIgnoreCase(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)){
            return new Admin();
        }

        User userFound = findUser(email);
        if (userFound == null || !userFound.getPassword().equals(password)){
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return userFound;
    }

    /**
     * Finds and return a user by comparing the email address.
     *
     * @param userEmail the email address being searched for.
     * @return User object or null.
     */
    private User findUser(String userEmail) {
        for (User user : customers) {
            if (user.getEmail().equalsIgnoreCase(userEmail)) {
                return user;
            }
        }
        return null;
    }

}
