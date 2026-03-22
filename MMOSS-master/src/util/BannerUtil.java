package util;

/**
 * A utility class that provides the
 * common banner and sub-banner in order to
 * display consistently across all the views.
 * This can be imported by the different boundary
 * classes to view.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */

public class BannerUtil
{
    /**
     * Method to print the header/banner of the app
     * for all the views
     *
     */
    public static void printHeader()
    {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" | Monash Merchant Online Supermarket System | ");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::\n");
    }

    /**
     * Method to print the subheader/subbanner of MMOSS app
     * for each of the views
     *
     * @param subHeading     String of sub-heading which needs to be
     *                       printed depending on the page.
     */
    public static void printSubheader(String subHeading)
    {
        System.out.println("---------------------------------------------------");
        System.out.println(subHeading);
        System.out.println("---------------------------------------------------\n");
    }

}
