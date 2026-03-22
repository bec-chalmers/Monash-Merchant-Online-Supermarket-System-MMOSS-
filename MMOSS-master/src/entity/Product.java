package entity;

/**
 * class stores general information about an item available for purchase,
 * including its ID, name, brand, price, quantity, and category/subcategory details.
 * If the product is a food item, additional information such as expiry date,
 * ingredients, storage instructions, and allergen info are stored in a FoodInfo object.

 * Each product belongs to a specific Subcategory, which in turn is linked Category.
 */
public class Product
{

    private String productId;
    private String name;
    private String brand;
    private String description;
    private double price;
    private double memberPrice;
    private int quantity;
    private Subcategory sub;
    private boolean isFood;
    private FoodInfo foodInfo;


    /**
     * Constructs a Product with the specified details.
     *
     * @param productId the unique identifier of the product
     * @param name the name of the product
     * @param brand the brand of the product
     * @param description a short description of the product
     * @param price the regular price of the product
     * @param memberPrice the discounted price for members
     * @param quantity the available stock quantity
     * @param sub the subcategory to which the product belongs
     * @param isFood true if the product is a food item, false otherwise
     * @param foodInfo additional food-related information
     */
    public Product(String productId, String name, String brand, String description,
                   double price, double memberPrice, int quantity, Subcategory sub,boolean isFood, FoodInfo foodInfo) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.memberPrice = memberPrice;
        this.quantity = quantity;
        this.sub = sub;
        this.isFood = isFood;
        this.foodInfo = foodInfo;
    }

    /**
     * Indicates whether this product is a food item.
     *
     * @return true if the product is food, otherwise false
     */
    public boolean isFood()
    {
        return isFood;
    }

    /**
     * Returns the product ID.
     *
     * @return the product ID
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * Updates the product ID.
     *
     * @param productId the new product ID
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * Returns the product name.
     *
     * @return the product name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Updates the product name.
     *
     * @param name the new product name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the product brand.
     *
     * @return the product brand
     */
    public String getBrand()
    {
        return brand;
    }

    /**
     * Updates the product brand.
     *
     * @param brand the new brand name
     */
    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    /**
     * Returns the regular price of the product.
     *
     * @return the product price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Updates the regular price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(double price)
    {
        this.price = price;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public Subcategory getSub()
    {
        return sub;
    }

    public void setSub(Subcategory sub)
    {
        this.sub = sub;
    }

    public FoodInfo getFoodInfo()
    {
        return foodInfo;
    }

    public void setFoodInfo(FoodInfo foodInfo)
    {
        this.foodInfo = foodInfo;
    }

    public double getMemberPrice()
    {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice)
    {
        this.memberPrice = memberPrice;
    }
    // do this
}
