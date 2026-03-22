package boundry;

import controllers.VIPMembershipController;
import entity.Customer;
import entity.VIPMembership;
import util.BannerUtil;

import java.util.Scanner;

/** A boundary class which deals with
 * VIP Membership operations (purchase, renew, cancel)
 * user interaction and view.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class VIPMembershipView {

    private final VIPMembershipController membershipController;

    /**
     * Non-Default Constructor for VIPMembershipView class.
     *
     * @param   customer    customer object
     */
    public VIPMembershipView(Customer customer)
    {
        this.membershipController = new VIPMembershipController(customer);
    }

    /**
     * Method for displaying the menu options
     * and for handling the user interactions of
     * VIP membership activities.
     *
     */
    public void vipMenu()
    {
        Scanner scanner = new Scanner(System.in);

        int choice = -1;
        do
        {
            BannerUtil.printHeader();
            BannerUtil.printSubheader("VIP Membership Options");

            // additional information for customer to know
            System.out.println("**VIP Membership @ AUD $" + VIPMembership.getVipCostPerYear() + " per year**");
            System.out.println("**Membership is non-refundable**");

            // menu starts here
            System.out.println("1. Purchase Membership (New) ");
            System.out.println("2. Renew Membership");
            System.out.println("3. Cancel Membership");
            System.out.println("4. Back to Home");
            System.out.print("Enter your choice (1-5): ");

            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Please enter a valid number.");
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice)
            {
                case 1:
                    purchaseNewVipView();
                    break;
                case 2:
                    renewVipView();
                    break;
                case 3:
                    cancelVipView();
                    break;
                case 4:
                    System.out.println("Returning to home...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 4);
    }

    /**
     * Method for displaying
     * and for handling the user interactions of
     * purchasing new membership by customer.
     *
     */
    public void purchaseNewVipView()
    {
        Scanner scanner = new Scanner(System.in);

        boolean inputFlag = false; // for loop until right

        while (!inputFlag)
        {
            System.out.print("Enter the number of years of membership you wish to purchase: ");
            String input = scanner.nextLine();

            try
            {
                int years = Integer.parseInt(input);
                membershipController.purchaseNewVip(years);
                BannerUtil.printHeader();
                BannerUtil.printSubheader("Purchase VIP Membership");
                System.out.println("Congratulations! You have purchased " + years + " year(s) of membership.");
                inputFlag = true; // no exception, purchase complete
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error: Please enter a valid number for years of membership.");
            }
            catch (IllegalArgumentException e) // catch wrong no of years
            {
                inputFlag = showYearErrorView(e.getMessage());
            }
            catch (Exception e)  // catch any other error including low funds
            {
                showGeneralErrorView(e.getMessage());
                inputFlag = true; // exit loop and go back
            }
        }
    }

    /**
     * Method for displaying
     * and for handling the user interactions of
     * renewing existing membership of customer.
     *
     */
    public void renewVipView() {
        Scanner scanner = new Scanner(System.in);
        boolean inputFlag = false;

        while (!inputFlag) {
            System.out.print("Enter the number of years of membership you wish to renew: ");
            String input = scanner.nextLine();

            try
            {
                int years = Integer.parseInt(input);
                membershipController.renewVIP(years);
                BannerUtil.printHeader();
                BannerUtil.printSubheader("Renew VIP Membership");
                System.out.println("Congratulations! Your membership has been renewed for " + years + " year(s).");
                inputFlag = true;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error: Please enter a valid number for years of membership.");
            }
            catch (IllegalArgumentException e)
            {
                inputFlag = showYearErrorView(e.getMessage());
            }
            catch (Exception e)
            {
                showGeneralErrorView(e.getMessage());
                inputFlag = true;
            }
        }
    }

    /**
     * Method for displaying
     * and for handling the user interactions of
     * cancelling existing membership of customer.
     *
     */
    public void cancelVipView()
    {
        try
        {
            membershipController.cancelVIP();
            BannerUtil.printHeader();
            BannerUtil.printSubheader("Cancel VIP Membership");
            System.out.println("Your membership has been cancelled successfully.");
        }
        catch (Exception e)
        {
            showGeneralErrorView(e.getMessage());
        }
    }

    /**
     * Method for showing options and handling
     * user interaction and response messages of
     * user entering wrong number of year for membership activity.
     *
     * @param   errorMessage    String of the error message to display
     * @return  boolean          true or false for continuing
     */
    private boolean showYearErrorView(String errorMessage)
    {
        Scanner scanner = new Scanner(System.in);
        boolean inputFlag = false;
        //showing error message
        System.out.println(" Error: " + errorMessage);
        // User Option
        System.out.println("Try entering number of years of membership again!");
        System.out.println("1. Enter new number of year of membership");
        System.out.println("2. Go Back");
        System.out.print("Enter your choice (1-2): ");

        // user option handling here
        int option = scanner.nextInt(); // if option 1, loop will run again to take input
        scanner.nextLine();
        if (option == 2)
        {
            inputFlag = true; // return back
        }
        else if (option != 1)
        {
            System.out.println("Invalid option. Going Back.");
            inputFlag = true;
        }
        return inputFlag; // retry
    }

    /**
     * Method for showing general error view which is used
     * repetitively by the different methods.
     *
     * @param   errorMessage    String of the error message to display
     */
    private void showGeneralErrorView(String errorMessage)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" Error: " + errorMessage);
        System.out.print("Press Enter to go back... ");
        scanner.nextLine();
    }


}