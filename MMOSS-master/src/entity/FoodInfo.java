package entity;

/**
 * This class stores attributes specific to food items,
 * such as expiry date, ingredients, storage instructions, and allergen information.
 * It is typically used as a supporting entity within a Product object
 */
public class FoodInfo
{

    private String expiryDate;
    private  String ingredients;
    private  String storageInstructions;
    private  String allergenInfo;


    /**
     * Constructs an object with the specified food-related details.
     *
     * @param expiryDate the expiry date of the food item
     * @param ingredients a description or list of ingredients
     * @param storageInstructions the recommended storage instructions
     * @param allergenInfo information about potential allergens
     */
    public FoodInfo(String expiryDate, String ingredients, String storageInstructions, String allergenInfo)
    {
        this.expiryDate = expiryDate;
        this.ingredients = ingredients;
        this.storageInstructions = storageInstructions;
        this.allergenInfo = allergenInfo;
    }

    /**
     * Returns the expiry date of the food item.
     *
     * @return the expiry date
     */
    public String getExpiryDate()
    {
        return expiryDate;
    }

    /**
     * Returns the ingredients of the food item.
     *
     * @return the ingredients
     */
    public String getIngredients()
    {
        return ingredients;
    }

    /**
     * Returns the storage instructions for the food item.
     *
     * @return the storage instructions
     */
    public String getStorageInstructions()
    {
        return storageInstructions;
    }

    /**
     * Returns the allergen information for the food item.
     *
     * @return the allergen information
     */
    public String getAllergenInfo()
    {
        return allergenInfo;
    }

    /**
     * Sets a new expiry date for the food item.
     *
     * @param expiryDate the updated expiry date
     */
    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    /**
     * Sets a new ingredient description for the food item.
     *
     * @param ingredients the updated ingredient list or description
     */
    public void setIngredients(String ingredients)
    {
        this.ingredients = ingredients;
    }

    /**
     * Sets new storage instructions for the food item.
     *
     * @param storageInstructions the updated storage instructions
     */
    public void setStorageInstructions(String storageInstructions)
    {
        this.storageInstructions = storageInstructions;
    }

    /**
     * Sets new allergen information for the food item.
     *
     * @param allergenInfo the updated allergen information
     */
    public void setAllergenInfo(String allergenInfo)
    {
        this.allergenInfo = allergenInfo;
    }

}
