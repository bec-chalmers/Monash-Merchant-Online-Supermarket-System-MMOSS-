package boundry;

import controllers.PromoController;
import entity.Customer;
import entity.Order;
import entity.Promotion;
import java.util.Scanner;
import util.BannerUtil;

import static util.ErrorUtil.showErrorScreen;

/**
 * Boundary class that handles
 * the displaying of the promo code
 * entry process to the user.
 *
 * @version 1.6
 * @author Rebecca Chalmers
 */
public class PromoView
{
    private final PromoController promoController;
    private final Customer customer;
    final Order order;
    private final boolean isPickupOrder;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs the promo code view.
     *
     * @param promoController controller used to find and validate promotions
     * @param customer        the logged-in customer
     * @param order           the current order being built
     * @param isPickupOrder   true if this is a pickup flow, false if delivery
     */
    public PromoView(PromoController promoController,
                     Customer customer,
                     Order order,
                     boolean isPickupOrder)
    {
        this.promoController = promoController;
        this.customer = customer;
        this.order = order;
        this.isPickupOrder = isPickupOrder;
    }

    /**
     * Shows the initial promo code decision screen.
     *
     * @return true if 'Proceed to Order Summary' is selected, false if user chooses to go home from a later screen
     */
    public boolean showPromoCodeScreen()
    {
        // Displays header and subheader + options
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Promo Codes");

        System.out.println("Would you like to enter a promo code?");
        System.out.println("\t1. Enter promo code");
        System.out.println("\t2. Proceed to Order Summary");
        System.out.println("\t3. Go back\n");
        System.out.print("Enter your choice (1-3): ");

        while (true)
        {
            // Validates input (for number)
            if (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1, 2, or 3).");
                System.out.print("Enter your choice (1-3): ");
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if  (choice == 1)       // User selects "1. Enter promo code"
            {
                return enterPromoCode();    // returns true if proceeding, and false if not
            }
            else if (choice == 2)   // User selects "2. Proceed to Order Summary"
            {
                return true;                // proceeds to order summary without entering a code
            }
            else if (choice == 3)   // User selects "3. Go back"
            {
                return false;
            }
            else                    // User provides invalid number
            {
                showErrorScreen("Please enter a number (1, 2, or 3).");
                System.out.print("Enter your choice (1-3): ");
            }

        }

    }

    /**
     * Prompts for a promo code, validates it, and applies it to the order if valid.
     *
     * @return true to continue to the Order Summary, false to return home.
     */
    public boolean enterPromoCode()
    {
        while (true)
        {
            System.out.print("\nEnter promo code (Only one promo code per order): ");
            String promoCode = scanner.nextLine().trim().toUpperCase();

            // Validates promo code
            Promotion promotion = promoController.findPromotionByCode(promoCode);
            boolean validity = promoController.validateCode(promoCode, customer, isPickupOrder);

            // Valid promo code
            if (promotion != null && validity)
            {
                Promotion previousPromotion = order.getPromo();
                order.setPromo(promotion);      // Applies new promo code

                if (previousPromotion != null && !previousPromotion.getCode().equalsIgnoreCase(promotion.getCode()))
                {
                    System.out.println("Replaced previous promo code: " + previousPromotion.getCode());
                }

                // Confirmation message
                System.out.println("\nYour promo code has been accepted.");
                System.out.println("\tPromo code: " + promotion.getCode() + " (" + promotion.getDescription() + ")");
                System.out.println("\nPress Enter to proceed to Order Summary...");
                scanner.nextLine();
                return true;
            }

            // Invalid promo code flow
            System.out.println("\nInvalid promo code.");
            System.out.println("\t1. Enter a new promo code");
            System.out.println("\t2. Proceed to Order Summary");
            System.out.println("\t3. Go back");
            System.out.print("Enter your choice (1-3): ");

            // Validate input (number)
            if  (!scanner.hasNextInt())
            {
                scanner.nextLine();
                showErrorScreen("Please enter a number (1-3).");
                continue;
            }

            int choice  = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1)
            {
                                // Loop again to re-enter promo code
            }
            else if (choice == 2)
            {
                return true;    // Proceed without promo code (because it didn't work)
            }
            else if (choice == 3)
            {
                return false;   // Back to home
            }
            else
            {
                showErrorScreen("Please enter a number (1-3).");
            }
        }
    }
}
