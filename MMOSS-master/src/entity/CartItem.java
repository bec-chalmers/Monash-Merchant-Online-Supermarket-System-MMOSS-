package entity;

/** An entity class CartItem which denotes
 * one item of the cart consisting of a product
 * and the quantity of that product added.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class CartItem
{
    private Product product;
    private int quantity;

    /**
     * Non-Default Constructor for Cart Item class.
     *
     * @param   product      The product object.
     * @param   quantity     The quantity of the product.
     */
    public CartItem(Product product, int quantity)
    {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Accessor Method of Cart Item to get the product.
     *
     * @return   Product    The product object.
     */
    public Product getProduct()
    {
        return product;
    }

    /**
     * Accessor Method of Cart Item to get the quantity.
     *
     * @return   int    The quantity of product.
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Mutator Method of Cart Item to set the product.
     *
     * @param   product    The product object.
     */
    public void setProduct(Product product)
    {
        this.product = product;
    }

    /**
     * Mutator Method of Cart Item to set the quantity.
     *
     * @param   quantity    The quantity of product object.
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
