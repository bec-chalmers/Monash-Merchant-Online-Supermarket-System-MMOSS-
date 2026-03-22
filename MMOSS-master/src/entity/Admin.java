package entity;

/**
 * Represents an administrator user in the system.
 * Admin has their own privileges
 */
public class Admin extends User
{
    /**
     * Creates a default admin user with predefined Monash credentials and contact details.
     * used for system initialization / testing.
     */
    public Admin()
    {
        super("admin@monash.edu", "Admin1234!", "Joshua", "Miller", "0423456789");
    }

    /**
     * creates a Admin obj with custom details.
     */
    public Admin(String adminEmail, String adminPassword, String admin, String admin1, String number) {
        super(adminEmail,adminPassword,admin,admin1,number);
    }

}
