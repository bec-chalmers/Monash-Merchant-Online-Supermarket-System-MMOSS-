package boundry;

import controllers.CartController;
import controllers.VIPMembershipController;
import entity.CartItem;
import entity.Customer;
import entity.Product;
import entity.VIPMembership;
import util.BannerUtil;

import java.util.*;

/** A boundary class which deals with
 * the cart views.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class CartView
{
    private CartController cartController;
    private final CustomerView customerView;

    /**
     * Non-Default Constructor for CartView class.
     *
     * @param   cartController    cartController object
     * @param   customerView      customerView object
     */
    public CartView(CartController cartController, CustomerView customerView)
    {
        this.cartController = cartController;
        this.customerView = customerView;
    }

    /**
     * Method which handles the view and user interaction
     * for the add to cart functionality. It takes the product
     * object to add to cart and handles user input.
     *
     * @param   product    Product object to add to cart.
     */
    public void addToCartView(Product product) {
        Scanner scanner = new Scanner(System.in);

        BannerUtil.printHeader();
        BannerUtil.printSubheader("Add to Cart");

        //printing the product details to add
        System.out.printf("%-20s : %s%n", "Name", product.getName());
        System.out.printf("%-20s : %s%n", "Brand", product.getBrand());
        System.out.printf("%-20s : %s%n", "Description", product.getDescription());
        System.out.printf("%-20s : $%.2f%n", "Price", product.getPrice());
        System.out.printf("%-20s : %d%n", "Stock Available", product.getQuantity());

        int quantity = 0;
        while (true) {
            System.out.print("Enter new quantity (or press Enter to cancel): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                System.out.println("Cart Editing cancelled. Returning to cart..");
                return; // cancelling and returning
            }
            try
            {
                quantity = Integer.parseInt(input);
                // cartController will do the checks needed
                cartController.addToCart(product, quantity);

                // VIP check and price showing
                boolean isVIP = checkVIPStatus();
                double unitPrice = isVIP ? product.getMemberPrice() : product.getPrice();
                double totalPrice = unitPrice * quantity;

                System.out.printf("\nProduct: %s%nPrice per unit: $%.2f%nQuantity: %d%nTotal Price: $%.2f%n",
                        product.getName(), unitPrice, quantity, totalPrice);

                System.out.println("\nProduct added to cart successfully!");
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Enter a valid number.");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
        pressEnterToContinue();
    }

    /**
     * Method which handles the view for showing the
     * cart status to the user. It shows if the cart is empty
     * and displays items if the cart is not empty.
     *
     */
    public void showCart()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader("View Cart Details");

        if (cartController.isCartEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        ArrayList<CartItem> items = cartController.getCart().getCartItems();

        boolean isVIP = checkVIPStatus(); // checking vip status

        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-10s %-10s %-10s%n", "No", "Product", "Qty", "Price", "Subtotal");

        double total = 0;
        int index = 1;

        for (CartItem item : items)
        {
            double unitPrice = isVIP ? item.getProduct().getMemberPrice() : item.getProduct().getPrice();
            double subtotal = unitPrice * item.getQuantity();
            total += subtotal;
            System.out.printf("%-5d %-25s %-10d $%-9.2f $%-9.2f%n",
                    index++, item.getProduct().getName(),
                    item.getQuantity(), item.getProduct().getPrice(), subtotal);
        }

        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("%-25s : $%.2f%n", "Total Amount", total);
    }

    /**
     * Method which handles the menu option of the cart.
     * It uses the showCart() function to show items/empty status
     * and handles user interaction for the cart functionalities of
     * cart menu (adding, editing, removing and so on).
     *
     * @param   findProductsView    FindProductsView object to allow user
     *                              to go back to browsing page.
     */
    public void viewCart(FindProductsView findProductsView)
    {
        Scanner scanner = new Scanner(System.in);

        ArrayList<CartItem> items = cartController.getCart().getCartItems();

        // if cart is empty, then this part
        if (items.isEmpty())
        {
            System.out.println("Your cart is empty.");
            System.out.println("\n1. Add to Cart (Go to Browse Products)");
            System.out.println("2. Back");
            System.out.print("Enter your choice (1-2): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1"))
            {
                findProductsView.defaultBrowsing();
            }
            return;
        }

        // if cart has items in it, we show cart items here
        showCart();

        // After Cart Items, Cart menu starts here
        System.out.println("\n1. Add Items to Cart");
        System.out.println("2. Edit Items in Cart");
        System.out.println("3. Remove Items from Cart");
        System.out.println("4. Empty Cart");
        System.out.println("5. Proceed to Checkout");
        System.out.println("6. Return to Home");
        System.out.print("Enter your choice (1-6): ");
        String choice = scanner.nextLine().trim();

        switch (choice)
        {
            case "1":
                findProductsView.defaultBrowsing();
                break;
            case "2":
                editCart();
                break;
            case "3":
                removeItemFromCart();
                break;
            case "4":
                emptyCart();
                break;
            case "5":
                customerView.beginCheckout();
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid choice input. Please try again.");
                break;
        }
    }

    /**
     * Method which handles the view and user interaction for
     * editing the current cart items quantity.
     *
     */
    public void editCart() {
        Scanner scanner = new Scanner(System.in);

        if (cartController.isCartEmpty())  // check for empty cart
        {
            System.out.println("Your cart is empty.");
            return;
        }

        ArrayList<CartItem> items = cartController.getCart().getCartItems();

        System.out.println("\nSelect the product to edit:"); // if not empty, we show cart items to choose
        for (int i = 0; i < items.size(); i++)
        {
            System.out.printf("%d. %s | Current Qty: %d%n",
                    i + 1, items.get(i).getProduct().getName(), items.get(i).getQuantity());
        }
        System.out.print("Enter product number: ");

        int index;
        try
        {
            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid input.");
            return;
        }

        if (index < 0 || index >= items.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        CartItem selectedItem = items.get(index);
        Product product = selectedItem.getProduct();

        System.out.printf("\nEditing '%s' (Available stock: %d)%n",
                product.getName(), product.getQuantity());
        System.out.printf("Current Quantity in Cart: %d%n", selectedItem.getQuantity());

        int newQuantity;
        while (true)
        {
            System.out.print("Enter new quantity (or press Enter to cancel): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
            {
                System.out.println("Cart Editing cancelled. Returning to cart..");
                return; // cancelling and returning
            }

            try
            {
                newQuantity = Integer.parseInt(input);
                cartController.updateCart(product, newQuantity); // controller will handle the editing validation

                // VIP check and price calculation
                boolean isVIP = checkVIPStatus();
                double unitPrice = isVIP ? product.getMemberPrice() : product.getPrice();
                double totalPrice = unitPrice * newQuantity;

                System.out.println("\n Cart updated successfully!");
                System.out.printf("Product: %s%nPrice per unit: $%.2f%nQuantity: %d%nTotal Price: $%.2f%n",
                        product.getName(), product.getPrice(), newQuantity, totalPrice);
                break;

            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid input. Enter a valid number.");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Error: " + e.getMessage()); // exception handling
            }
        }

        pressEnterToContinue();
    }

    /**
     * Method which handles the view and user interaction for
     * removing the cart items.
     *
     */
    public void removeItemFromCart()
    {
        Scanner scanner = new Scanner(System.in);

        if (cartController.isCartEmpty())  // check if cart empty
        {
            System.out.println("Your cart is empty.");
            return;
        }
        ArrayList<CartItem> items = cartController.getCart().getCartItems();

        System.out.println("\nSelect the product to remove:"); // if not empty, we show cart items to choose
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s | Qty: %d%n",
                    i + 1,
                    items.get(i).getProduct().getName(),
                    items.get(i).getQuantity());
        }

        System.out.print("Enter product number (or press Enter to cancel): ");
        String input = scanner.nextLine().trim();

        // If user presses enter, we cancel and return
        if (input.isEmpty()) {
            System.out.println("Removal cancelled. Returning to cart..");
            return;
        }

        try {
            int index = Integer.parseInt(input) - 1;
            if (index < 0 || index >= items.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Product product = items.get(index).getProduct();

            // Confirmation before removal
            System.out.print("Are you sure you want to remove this item? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("y")) {
                cartController.removeFromCart(product);
                System.out.println("Cart updated successfully.");
            } else {
                System.out.println("Cancelled Item Removal.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        showCart();

        pressEnterToContinue();
    }

    /**
     * Method which handles the view and user interaction for
     * emptying the cart currently.
     *
     */
    public void emptyCart()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIf you proceed with this action, all items saved in your cart will be removed.");
        System.out.print("\nAre you sure you want to empty your cart? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y"))
        {
            cartController.getCart().clearCart();
            System.out.println("\nCart has been emptied successfully.");
        }
        else
        {
            System.out.println("Cancelled Action- Empty cart.");
        }

        pressEnterToContinue();
    }

    /**
     * Method which allows user to Enter to continue.
     * It is used repeatedly in the functions.
     *
     */
    private void pressEnterToContinue()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Method which checks whether the customer is VIP
     * and returns the VIP active status for price calculation.
     *
     * @return  boolean        True if VIP member.
     */
    private boolean checkVIPStatus() {
        Customer customer = (Customer) customerView.getLoggedInUser();
        if (customer == null)
            return false;
        VIPMembershipController vipController = new VIPMembershipController(customer);
        return vipController.hasActiveVIP();
    }

}
