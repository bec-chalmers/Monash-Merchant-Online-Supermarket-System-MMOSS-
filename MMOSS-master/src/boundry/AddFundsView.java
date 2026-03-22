package boundry;

import controllers.AddFundsController;
import entity.Customer;
import util.BannerUtil;
import java.util.Scanner;

import static util.ErrorUtil.showErrorScreen;

public class AddFundsView
{
    private final AddFundsController controller = new AddFundsController();
    private final Scanner scanner = new Scanner(System.in);

    public void displayAddFundsMenu(Customer customer)
    {
        if (customer == null)
        {
            System.out.println("No active customer found.");
            return;
        }

        while (true)
        {
            BannerUtil.printHeader();
            BannerUtil.printSubheader("Add Funds");

            System.out.println("Would you like to add funds to your account balance?");
            System.out.println("\t*Transactions must be at least AUD $1");
            System.out.println("\t*Transactions are capped at AUD $1000");
            System.out.println();
            System.out.print("Enter amount to add to account balance (Press 0 to cancel): ");

            String line = scanner.nextLine().trim();

            double amount;

            try
            {
                amount = Double.parseDouble(line);
            }
            catch (NumberFormatException e)
            {
                showErrorScreen("Invalid input. Please enter a number (or 0 to cancel)");
                continue;
            }

            if (amount == 0.0)
            {
                System.out.println();
                System.out.println("\tPress Enter to return to home...");
                scanner.nextLine();
                return;
            }

            if (amount < 1.0 || amount > 1000.0)
            {
                showErrorScreen("Amount must be between 1 and 1000.");
                continue;
            }

            boolean ok = controller.addFunds(customer, amount);
            System.out.println("\n--------------------------------------------------------------------------\n");

            if (ok)
            {
                System.out.printf("AUD $%,.2f has been successfully added to your account balance%n", amount);
                System.out.printf("Current balance: $%,.2f%n%n", customer.getBalance());
                System.out.println("\tPress Enter to return to home...");
                scanner.nextLine();
                return;
            }
            else
            {
                showErrorScreen("Transaction failed. Please try again.");
            }
        }
    }
}