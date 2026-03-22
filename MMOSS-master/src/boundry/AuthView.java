package boundry;
import controllers.AuthenticationController;
import controllers.PickupController;
import controllers.ProductController;
import controllers.PromoController;
import entity.Admin;
import entity.Customer;
import entity.User;
import util.BannerUtil;
import java.util.Scanner;
import static util.ErrorUtil.showErrorScreen;

/**
 * The boundary class used to represent user interaction when login in.
 *
 * @author Yapa Jayasinghe
 * @version 1.0
 */
public class AuthView
{

    private final Scanner scanner = new Scanner(System.in);
    private final AuthenticationController authController;
    private final ProductController productController;
    private final PickupController pickupController;
    private final PromoController promoController;

    /**
     * Constructs a AuthView obj instance with the relevant controllers being passed in.
     * @param authController AuthenticationController Obj responsible for handling user authentication.
     * @param productController ProductController Obj responsible for handling product related operations.
     * @param pickupController PickupController Obj responsible for pickup operations.
     * @param promoController PromoController Obj responsible for handling promotions.
     */
    public AuthView(AuthenticationController authController,
                    ProductController productController,
                    PickupController pickupController,
                    PromoController promoController) {
        this.authController = authController;
        this.productController = productController;
        this.pickupController = pickupController;
        this.promoController = promoController;
    }

    /**
     * Starting point - makes sure the menu is shown and,
     * the selected menu option triggers the relevant action / method.
     * Method will run continuously until the user selects 0
     */
    public void start()
    {
        while (true) {
            int choice = selectedMenuOption();
            switch (choice) {
                case 1: userLogin();
                    break;
                case 2: userRegister();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    /**
     * Displays the menu for authentication and gets the user option selected.
     * Ensures that the input is in fact an integer and restricted to the option range.
     *
     * @return int the selected menu option number.
     */
    public int selectedMenuOption()
    {
        BannerUtil.printHeader();
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Quit");
        while (true)
        {
            System.out.print("Choose an option (0/1/2): ");

            // ensure numeric
            if (!scanner.hasNextInt())
            {
                scanner.nextLine(); // discards invalid line
                showErrorScreen("Please enter a number (0, 1, or 2).");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            // ensure in range
            if (choice == 0 || choice == 1 || choice == 2)
            {
                return choice;
            }

            showErrorScreen("Invalid choice. Please enter only 0, 1, or 2.");
        }
    }

    /**
     * Prompts the user with login details.
     * Makes use of the AuthenticationController instance to validate credentials.
     */
    public void userLogin()
    {

        boolean loggedIn = false;

        while (!loggedIn)
        {
            System.out.print("Enter Monash Email : ");
            String email = scanner.nextLine();
            System.out.print("Enter Password : ");
            String password = scanner.nextLine();

            try
            {
                User user = authController.authenticateUser(email, password);
                if (user != null)
                {
                    loggedIn = true;
                    //System.out.println(user.toString()); // todo: implement the view for successful login
                    // the next view is being decided from what type of user it is
                    if (user instanceof Admin)
                    {
                        new AdminView(user, productController).showAdminMenu();
                    }
                    else if (user instanceof Customer) {
                        new CustomerView(user, productController, pickupController, promoController).showCustomerMenu(); // temp checkout connection
                    }
                }
            } catch (RuntimeException e) {
                showErrorScreen(e.getMessage());
            }
        }

    }

    /**
     * Handles user registration - currently out of scope.
     */
    public void userRegister() {
        // out of scope for this assignment ! - not implemented
        // test push - for account issue.
    }

}
