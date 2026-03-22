package entity;

import java.util.List;

/**
 * Represents a product category within the system.
 * Each Category contains a name and a list of associated Subcategory objects.
 * Categories help organize products for easier browsing and management.
 */
public class Category {
    private String categoryName;
    private List<Subcategory> subcategories;

    /**
     * Constructs a new Category Obj with the specified name and list of subcategories.
     *
     * @param categoryName the name of the category (e.g., "Beverages", "Snacks")
     * @param subcategories the list of subcategories that belong to this category
     */
    public Category(String categoryName, List<Subcategory> subcategories) {
        this.categoryName = categoryName;
        this.subcategories = subcategories;
    }

    /**
     *
     * @return String name of the category.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     *
     * @param categoryName String category name to which the category name is being set to.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     *
     * @return List<Subcategory> return the list of subcategories.
     */
    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    /**
     *
     * @param subcategories String value used to set subcategories.
     */
    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
