package entity;

import java.util.*;

/** An entity class which deals with
 * the Cart entity which consist of list of
 * cart item objects.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class Cart
{
    private ArrayList<CartItem> cartItems;

    /**
     * Default Constructor for Cart class.
     *
     */
    public Cart()
    {
        this.cartItems = new ArrayList<>();
    }

    /**
     * Method of Cart used to create a CartItem object from
     * the product and quantity and add it to the arraylist of
     * cartItem objects in Cart.
     *
     * @param   product    The product object
     * @param   quantity    The quantity of product to add.
     */
    public void addCartItem(Product product, int quantity)
    {
        for (CartItem item: cartItems)
        {
            // matches with the product id
            if(item.getProduct().getProductId() == product.getProductId())
            {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity)); // add to list
    }

    /**
     * Method of Cart used to clear the cart items
     * from the Cart.
     * This part of the code is AI generated.
     *
     */
    public void clearCart()
    {
        cartItems.clear();
    }

    /**
     * Method of Cart to get the total number of
     * one product (passed as parameter) in Cart.
     *
     * @return  integer  total no. of a product as integer.
     * @param   product  Product object whose quantity is needed.
     */
    public int cartProductQuantity(Product product)
    {
        for (CartItem item : cartItems)
        {
            if (item.getProduct().equals(product))
            {
                return item.getQuantity();
            }
        }
        return 0; // if it does not exist
    }

    /**
     * Method of Cart to calculate the
     * total number of items in the cart.
     *
     * @return  integer  total no. of item as integer.
     */
    public int cartQuantity()
    {
        int total = 0;
        // iterate and count the number of cart items
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Accessor Method of Cart to get the list of cart items.
     *
     * @return   ArrayList<CartItem>    The array list of cart item objects.
     */
    public ArrayList<CartItem> getCartItems()
    {
        return cartItems;
    }

    /**
     * Method of Cart used to remove a CartItem object from
     * the arraylist of cart items. The cart item to remove
     * will be identified using the product object passed as
     * parameter. This part of the code is AI generated.
     *
     * @param   product    The product object to remove
     * @return  boolean     True if successful
     */
    public boolean removeCartItem(Product product)
    {
        return cartItems.removeIf(item -> item.getProduct().equals(product));
    }

    /**
     * Mutator Method of Cart to set the list of cart items.
     *
     * @param   cartItems    The array list of cart item objects.
     */
    public void setCartItems(ArrayList<CartItem> cartItems)
    {
        this.cartItems = cartItems;
    }

}
