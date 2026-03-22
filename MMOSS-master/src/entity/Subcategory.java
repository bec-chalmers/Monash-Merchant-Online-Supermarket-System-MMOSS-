package entity;

/**
 * Represents a subcategory within a product category.
 * Each Subcategory belongs to a specific Category and helps further
 * organize products within that category. For example, a "Beverages" category may
 * have subcategories such as "Soft Drinks" or "Juices".
 */
public class Subcategory
{
    private String subCatName;
    private Category category;

    /**
     * Constructs a Subcategory with the specified name and parent category.
     *
     * @param subCatName the name of the subcategory (e.g., "Soft Drinks", "Juices")
     * @param category the parent  Category this subcategory belongs to
     */
    public Subcategory(String subCatName, Category category)
    {
        this.subCatName = subCatName;
        this.category = category;
    }

    /**
     * Returns the name of this subcategory.
     *
     * @return the subcategory name
     */
    public String getSubCatName()
    {
        return subCatName;
    }

    /**
     * Returns the parent category this subcategory belongs to.
     *
     * @return the parent Category
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * Sets a new name for this subcategory.
     *
     * @param subCatName the updated subcategory name
     */
    public void setSubCatName(String subCatName)
    {
        this.subCatName = subCatName;
    }

    /**
     * Sets a new parent category for this subcategory.
     *
     * @param category the updated parent Category
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }

}
