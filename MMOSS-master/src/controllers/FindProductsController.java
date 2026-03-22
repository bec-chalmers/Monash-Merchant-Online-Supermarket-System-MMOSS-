package controllers;
import entity.Product;
import entity.Subcategory;

import java.util.*;
import java.util.ArrayList;

/** A controller class which deals with
 * the logic of browsing and filtering
 * products for user.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class FindProductsController
{
    private final ProductController productController;

    public FindProductsController()
    {
        this.productController = new ProductController();
    }

    public FindProductsController(ProductController productController)
    {
        this.productController = productController;
    }

    /**
     * Method for the default browsing function
     * which returns the list of product objects for a category.
     * This method is used for filter by category as well.
     *
     * @param   categoryName        The name of category for which product list obtained.
     * @return  ArrayList<Product>    An Arraylist of product objects.
     */
    public ArrayList<Product> browseByCategory(String categoryName)
    {
        ArrayList<Product> productResult = new ArrayList<Product>();
        for (Product product: productController.getProductList())
        {
            if (product.getSub().getCategory().getCategoryName().equals(categoryName))
            {
                productResult.add(product);
            }
        }
        return productResult;
    }

    /**
     * Accessor method for returning the productController object.
     *
     * @return  ProductController    The productController object.
     */
    public ProductController getProductController() {
        return productController;
    }

    /**
     * Method which contains logic and returns the list of products for a particular
     * subcategory of a category. This list of product will be used in View to
     * display to user.
     *
     * @param   categoryName        The name of category for which product list obtained.
     * @param   subcategoryName     The String of subcategory for the category
     * @return  ArrayList<Product>    An Arraylist of product objects of the category and subcategory
     */
    public ArrayList<Product> filterSubcategory(String categoryName, String subcategoryName)
    {
        Subcategory subcategory = productController.findSubcategory(subcategoryName, categoryName);
        ArrayList<Product> productResult = new ArrayList<>();

        if (subcategory != null)
        {
            for (Product p : productController.getProductList())
            {
                if (p.getSub() != null &&
                        p.getSub().getSubCatName().equalsIgnoreCase(subcategory.getSubCatName()))
                {
                    productResult.add(p);
                }
            }
        }
        return productResult;
    }

    /**
     * Method which finds the list of all subcategories
     * for the category selected by the user for the filter
     * by subcategory option.
     *
     * @param   categoryName        The category name user selected as String.
     * @return  ArrayList<String>  An arraylist of all the subcategories as String.
     */
    public ArrayList<String> subOfCategory(String categoryName) {
        ArrayList<String> subcategoryNames = new ArrayList<>();
        for (Subcategory sub : productController.getSubcategoryList())
        {
            if (sub.getCategory().getCategoryName().equalsIgnoreCase(categoryName))
            {
                subcategoryNames.add(sub.getSubCatName()); //list of subcategory names for the selected category
            }
        }
        return subcategoryNames;
    }

    /**
     * Method which provides all the list of brands to display to user
     * in the respective view function for the option of
     * filter by brand.
     *
     * @return   ArrayList<String>  An arraylist of all the brands as String.
     */
    public ArrayList<String> listAllBrands()
    {
        ArrayList<String> brandList = new ArrayList<>();
        // we get list of brands available from all products
        for (Product product : productController.getProductList())
        {
            String brand = product.getBrand();
            // removing null values, empty string, and duplicates
            if (brand != null && !brand.trim().isEmpty() && !brandList.contains(brand))
            {
                brandList.add(brand);
            }
        }
        Collections.sort(brandList); // sorting the brands in ascending order

        return brandList;
    }

    /**
     * Method which contains logic and returns the list of products for a particular
     * brand which user selected to filter by. This list of product will be used in
     * View to display to user.
     *
     * @param   brandName        The name of brand for which product list obtained.
     * @return  ArrayList<Product>    An Arraylist of product objects of selected brand.
     */
    public ArrayList<Product> filterByBrand(String brandName)
    {
        ArrayList<Product> productsOfBrand = new ArrayList<>();
        for (Product product : productController.getProductList())
        {
            if (product.getBrand().equalsIgnoreCase(brandName))
            {
                productsOfBrand.add(product);
            }
        }

        return productsOfBrand;
    }

    /**
     * Method which contains logic and returns the list of products for a particular
     * price range which user selected to filter by. The method takes minimum and
     * maximum price of the range, and finds products with a price within the
     * range. This list of product will be used in the view to display to user.
     *
     * @param   minPrice        The minimum price of the selected price range.
     * @param   maxPrice        The maximum price of the selected price range.
     * @return  ArrayList<Product>    An Arraylist of product objects of selected price range.
     */
    public ArrayList<Product> filterByPriceRange(double minPrice, double maxPrice)
    {
        ArrayList<Product> filteredProducts = new ArrayList<>();

        for (Product product : productController.getProductList())
        {
            double price = product.getPrice();

            // handle upper bound infinity for last range
            if (price >= minPrice && (maxPrice == Double.MAX_VALUE || price <= maxPrice))
            {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    /**
     * Method which contains logic and returns the list of products which are available
     * currently (stock quantity is more than 0). This list of product will be used in
     * View to display to user.
     *
     * @return  ArrayList<Product>    An Arraylist of product objects which are
     *                                available in stock.
     */
    public ArrayList<Product> filterByAvailability()
    {
        ArrayList<Product> availableProducts = new ArrayList<>();

        for (Product product : productController.getProductList())
        {
            // quantity shows availability, if product has 0 quantity, it is out of stock.
            if (product.getQuantity() > 0)
            {
                availableProducts.add(product);
            }
        }

        return availableProducts;
    }

}
