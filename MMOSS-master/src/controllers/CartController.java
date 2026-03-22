package controllers;

import entity.Cart;
import entity.CartItem;
import entity.Product;

/** A controller class which deals with
 * the logic for all cart operations.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class CartController
{
    private Cart cart;

    /**
     * Default Constructor for CartController class.
     *
     */
    public CartController()
    {
        this.cart = new Cart();
    }

    /**
     * Non-Default Constructor for CartController class.
     *
     * @param   cart    Cart object
     */
    public CartController(Cart cart)
    {
        this.cart = cart;
    }

    /**
     * Accessor method for cartController
     * which returns the cart object.
     *
     * @return   Cart    Cart object
     */
    public Cart getCart()
    {
        return cart;
    }

    /**
     * Method which contains logic for Add to Cart Function.
     * It takes the product and the quantity to add to cart
     * for a customer.
     *
     * @param   product      The Product object which user wants to add.
     * @param   quantity     The quantity of the product which the user wants
     *                       to add.
     */
    public void addToCart(Product product, int quantity)
    {
        // checking is user put in negative quantity
        checkInputQuantity(quantity);
        // checking if user put quantity more than the stock number
        checkStock(product, quantity);

        // checking - 10 units allowed per individual product.
        int currentQuantity = cart.cartProductQuantity(product);
        if (currentQuantity + quantity > 10) {
            throw new IllegalArgumentException("You can only add up to 10 units of a single product.");
        }
        // checking - each shopping cart can have a maximum of 20 items
        int totalItems = cart.cartQuantity();
        if (totalItems + quantity > 20) {
            throw new IllegalArgumentException("Your cart cannot have more than 20 total items.");
        }

        // if all check passed, we add to cart
        cart.addCartItem(product, quantity);
    }

    /**
     * Method which checks if the cart is
     * empty. It returns true if empty.
     *
     * @return   boolean    True/False
     */
    public boolean isCartEmpty()
    {
        return cart.getCartItems().isEmpty(); //if true, then empty
    }

    public void updateCart(Product product, int updatedQuantity)
    {
        checkInputQuantity(updatedQuantity);
        checkStock(product, updatedQuantity);

        int currentQuantity = cart.cartProductQuantity(product);
        int previousQuantity = cart.cartQuantity() - currentQuantity;

        if (updatedQuantity > 10)
        {
            throw new IllegalArgumentException("Maximum 10 units per product can be added.");
        }

        if (previousQuantity + updatedQuantity > 20)
        {
            throw new IllegalArgumentException("Your cart cannot have more than 20 total items.");
        }

        // Update item quantity in cart
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().equals(product)) {
                item.setQuantity(updatedQuantity);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart.");
    }

    /**
     * Method which removes a product from cart.
     *
     * @param   product    The product object to be removed
     */
    public void removeFromCart(Product product)
    {
        boolean removed = cart.removeCartItem(product);

        if (!removed)  // if unsuccessful, we throw exception
        {
            throw new IllegalArgumentException("Product not found in cart.");
        }
    }

    /**
     * Method which checks the quantity given to
     * add to cart. Raises exception if quantity is
     * negative.
     *
     * @param   quantity   Quantity added
     */
    private void checkInputQuantity(int quantity)
    {
        if (quantity <= 0)
        {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }

    /**
     * Method which checks the quantity in stock for the
     * product which needs to be added to cart. Raises exception
     * if not enough in stock.
     *
     * @param   product    Product object.
     * @param   quantity   Quantity added
     */
    private void checkStock(Product product, int quantity) {
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }
    }

}
