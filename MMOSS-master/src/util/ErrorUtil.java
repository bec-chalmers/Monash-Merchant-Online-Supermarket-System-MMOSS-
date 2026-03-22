package util;

import java.util.Scanner;

/**
 * Boundary class that can display an
 * error message view to the user.
 *
 * @version 1.4
 * @author Rebecca Chalmers
 */
public class ErrorUtil
{
    /**
     * Shows an error message and waits for the user to press Enter.
     *
     * @param errorMessage the message to display
     */
    public static void showErrorScreen(String errorMessage)
    {
        BannerUtil.printHeader();

        // Prints error message to user and prompts them to press Enter
        System.out.println("*** Error: " + errorMessage + " ***\n");
        System.out.println("Press Enter to go back...");

        // Waits until user presses Enter
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}