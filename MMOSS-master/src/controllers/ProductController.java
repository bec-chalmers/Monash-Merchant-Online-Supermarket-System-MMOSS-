package controllers;
import entity.*;
import util.FileStorageUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for managing product related operations.
 * Loads the product data to memory.
 * Responsible for maintaining the business rules / validations related to actions taken against products.
 *
 * @author Yapa Jayasinghe
 * @version 1.1
 */
public class ProductController
{

    private final List<Product> productList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();
    private final List<Subcategory> subcategoryList = new ArrayList<>();

    /**
     * Ensures that the categories, subcategories and products are loaded on instantiation.
     */
    public ProductController()
    {
        try
        {
            loadCategoriesAndSubcategories();
            loadProducts();
        }
        catch (IOException e)
        {
            // wraps the IOException thrown by the lower level - originating from the util class.
            throw new RuntimeException("Failed to load product data!.", e);
        }
    }

    /**
     * Add new product to the in memory list of products.
     * Produces a String to represent one row of the product csv file.
     * In this method the product data is appended to the existing csv file.
     * @param name String name of the product
     * @param brand String brand name of the product
     * @param description String description of the product
     * @param price double price of the product
     * @param memberPrice double membership price of the product
     * @param quantity int quantity of the product
     * @param categoryName String name of the product category
     * @param subCategoryName String name of the product subcategory
     * @param isFood boolean whether product is food or not.
     * @param expiryDate String date of expiry for the product.
     * @param ingredientList String for ingredients.
     * @param storageInstructions String for storage instructions.
     * @param allergenInfo String for allergen information.
     */
    public void addNewProduct(String name, String brand, String description, double price,
            double memberPrice, int quantity, String categoryName, String subCategoryName, boolean isFood,
            String expiryDate, String ingredientList, String storageInstructions, String allergenInfo
    ) throws IOException {
        // validating category and subcategory details - should match existing
        Category category = findCategory(categoryName);
        if (category == null)
        {
            throw new IllegalArgumentException("Category "+categoryName+" does not exist!");
        }
        Subcategory subcategory = findSubcategory(subCategoryName,categoryName);
        if (subcategory == null)
        {
            throw new IllegalArgumentException("Subcategory "+subCategoryName+" does not exist!");
        }

        String newProductId = createNewId();

        FoodInfo foodInfo = null;
        if (isFood)
        {
            foodInfo = new FoodInfo(expiryDate,ingredientList,storageInstructions,allergenInfo);
        }

        // checking if the product is an already existing product or not
        for (Product existingProduct : productList){
            if (existingProduct.getName().equalsIgnoreCase(name)
            && existingProduct.getSub().equals(subcategory))
            {
                throw new IllegalArgumentException("Product "+existingProduct.getName()+" already exists!");
            }
        }

        // creating the new product obj
        Product newProduct =  new Product(newProductId,name, brand, description, price, memberPrice, quantity, subcategory, isFood, foodInfo);
        productList.add(newProduct);

        // saving to csv file
        List<String> quotedValues = new ArrayList<>();
        List<String> productValuesList = productToListOfStrings(newProduct);
        for (String productValue : productValuesList)
        {
            quotedValues.add(quoteIfNeeded(productValue));
        }
        String lineForCSV = String.join(",", quotedValues);
        FileStorageUtil.appendToFile("data/Products.txt", lineForCSV);

    }

    /**
     * Updates an existing product from the loaded product list.
     * @param prodId String id of the already existing product.
     * @param name String name of the product
     * @param brand String brand name of the product
     * @param description String description of the product
     * @param price double price of the product
     * @param memberPrice double membership price of the product
     * @param quantity int quantity of the product
     * @param categoryName String name of the product category
     * @param subCategoryName String name of the product subcategory
     * @param isFood boolean whether product is food or not.
     * @param expiryDate String date of expiry for the product.
     * @param ingredientList String for ingredients.
     * @param storageInstructions String for storage instructions.
     * @param allergenInfo String for allergen information.
     */
    public void updateProduct(String prodId,String name, String brand, String description, double price,
                              double memberPrice, int quantity, String categoryName, String subCategoryName,
                              boolean isFood, String expiryDate, String ingredientList, String storageInstructions,
                              String allergenInfo) throws IOException
    {

        // getting the matching product object from the in memery product list using the product id.
        Product productToEdit = null;
        for (Product existingProduct : productList){
            if(existingProduct.getProductId().equals(prodId))
            {
                productToEdit = existingProduct;
                break;
            }
        }

        if (productToEdit != null) {
            // using setters to set the updated values
            if (!name.isEmpty()) productToEdit.setName(name);
            if (!brand.isEmpty()) productToEdit.setBrand(brand);
            if (!description.isEmpty()) productToEdit.setDescription(description);

            if (price != -1) productToEdit.setPrice(price);
            if (memberPrice != -1) productToEdit.setMemberPrice(memberPrice);
            if (quantity != -1) productToEdit.setQuantity(quantity);

            // updating category and subcategory
            if (!categoryName.isEmpty())
            {
                Category selectedCategory = findCategory(categoryName);
                if (selectedCategory != null)
                {
                    // only change subcategory if explicitly given
                    if (subCategoryName != null && !subCategoryName.isEmpty())
                    {
                        Subcategory selectedSub = findSubcategory(subCategoryName, selectedCategory.getCategoryName());
                        if (selectedSub != null)
                        {
                            productToEdit.setSub(selectedSub);
                        }
                    }
                }
            }

            // updating food info of food category products
            if (isFood && productToEdit.isFood())
            {
                if (!expiryDate.isEmpty()) productToEdit.getFoodInfo().setExpiryDate(expiryDate);
                if (!ingredientList.isEmpty()) productToEdit.getFoodInfo().setIngredients(ingredientList);
                if (!storageInstructions.isEmpty()) productToEdit.getFoodInfo().setStorageInstructions(storageInstructions);
                if (!allergenInfo.isEmpty()) productToEdit.getFoodInfo().setAllergenInfo(allergenInfo);
            }

            // rewriting the whole list of products to csv file.
            List<String> csvLines = new ArrayList<>();

            // adding the header
            csvLines.add("product_id,name,brand,description,price,member_price,quantity,category_name,subcategory_name,is_food,expiry_date,ingredient_list,storage_instructions,allergen_info");

            for (Product product : productList) {
                List<String> quotedValues = new ArrayList<>();
                List<String> productValuesList = productToListOfStrings(product);
                for (String productValue : productValuesList)
                {
                    quotedValues.add(quoteIfNeeded(productValue));
                }
                String lineForCSV = String.join(",", quotedValues);
                csvLines.add(lineForCSV);
            }
            FileStorageUtil.writeToFile("data/Products.txt", csvLines);
        }
    }

    /**
     * Removes a product with the specified product ID from the product list and updates the product CSV file accordingly.
     * @param prodId prodId the unique identifier of the product to be removed.
     * @throws IOException if an error occurs while writing the updated product list to the file
     */
    public void removeProduct(String prodId) throws IOException
    {
        Product productToRemove = null;
        for (Product existingProduct : productList)
        {
            if (existingProduct.getProductId().equalsIgnoreCase(prodId))
                productToRemove = existingProduct;
        }
        if (productToRemove == null)
        {
            throw new IllegalArgumentException("Product does not exist!");
        }
        productList.remove(productToRemove);
        // rewriting the whole list of products to csv file.
        List<String> csvLines = new ArrayList<>();

        // adding the header
        csvLines.add("product_id,name,brand,description,price,member_price,quantity,category_name,subcategory_name,is_food,expiry_date,ingredient_list,storage_instructions,allergen_info");

        for (Product product : productList) {
            List<String> quotedValues = new ArrayList<>();
            List<String> productValuesList = productToListOfStrings(product);
            for (String productValue : productValuesList)
            {
                quotedValues.add(quoteIfNeeded(productValue));
            }
            String lineForCSV = String.join(",", quotedValues);
            csvLines.add(lineForCSV);
        }
        FileStorageUtil.writeToFile("data/Products.txt", csvLines);

    }


    /**
     * Transforms the object attributes in to a list of Strings so that it can be written to the csv.
     *
     * @param product the new Product object being created.
     * @return A list of Strings containing the attribute values of the product object.
     */
    private List<String> productToListOfStrings(Product product)
    {
        List<String> list = new ArrayList<>();

        list.add(product.getProductId());
        list.add(product.getName());
        list.add(product.getBrand());
        list.add(product.getDescription());
        list.add(String.valueOf(product.getPrice()));
        list.add(String.valueOf(product.getMemberPrice()));
        list.add(String.valueOf(product.getQuantity()));
        list.add(product.getSub().getCategory().getCategoryName());
        list.add(product.getSub().getSubCatName());
        list.add(String.valueOf(product.isFood()));

        if (product.isFood())
        {
            list.add(product.getFoodInfo().getExpiryDate());
            list.add(product.getFoodInfo().getIngredients());
            list.add(product.getFoodInfo().getStorageInstructions());
            list.add(product.getFoodInfo().getAllergenInfo());
        }else {
            list.add("");
            list.add("");
            list.add("");
            list.add("");
        }

        return list;
    }

    /**
     * Adds double quotes around a value if it contains spaces, commas or special characters.
     * @param value the String - product attribute to be quoted if condition met.
     * @return String formated in between with double quotes
     */
    private String quoteIfNeeded(String value) {
        if (value == null) return "";

        // Remove any existing surrounding quotes
        value = value.trim().replaceAll("^\"|\"$", "");

        // Escape internal quotes by doubling them
        String escaped = value.replace("\"", "\"\"");

        // Add quotes only if the value contains a space, comma, or quote
        if (escaped.contains(" ") || escaped.contains(",") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    /**
     * Generates the unique id by taking the highest id value from the existing list of products.
     * @return String value of the next highest prodId number.
     */
    private String createNewId()
    {
        int maxProdId = 0;
        for  (Product existingProduct : productList)
        {
            String id = existingProduct.getProductId();
            int intId = Integer.parseInt(id.substring(1));
            if (intId > maxProdId)
            {
                maxProdId = intId;
            }
        }
        return "P" + (maxProdId + 1);
    }

    /**
     * Loads the raw Categories and Subcategories  as lists of Strings from the relevant csv files.
     * to create the corresponding list of objects.
     * first loads the categories from "data/Category.txt"  and
     * uses them to associate subcategories from "data/SubCategory.txt"
     * Each subcategory has a reference to its parent Category.
     */
    public void loadCategoriesAndSubcategories() throws IOException
    {
        List<String> rawCategoryData = FileStorageUtil.readSingleColumnFile("data/Category.txt");
        List<List<String>> rawSubCategoryData = FileStorageUtil.readMultiColumnFile("data/SubCategory.txt");

        // to create the category objects
        for (String categoryName : rawCategoryData)
        {
            Category category = new Category(categoryName, new ArrayList<>());
            categoryList.add(category);
        }

        // to create the subcategory objects
        for (List<String> subCategoryData : rawSubCategoryData)
        {
            if (subCategoryData.size() != 2)
            {
                throw new IllegalArgumentException("Invalid subCategory record - should consist relevant category");
            }
            String subCategoryName = subCategoryData.get(0);
            String categoryName = subCategoryData.get(1);
            Category category = findCategory(categoryName);

            // checks if category is null in case of NullPointerException
            if (category == null)
            {
                throw new IllegalStateException("Unknown category '" + categoryName + "' in SubCategory.txt");
            }

            Subcategory subcategory = new Subcategory(subCategoryName, category);
            subcategoryList.add(subcategory);
        }

    }

    /**
     * Loads all the product data from "data/Products.txt" - creates a list of Product objects.
     * If the product is a food item a corresponding FoodInfo object is attached.
     */
    public void loadProducts() throws IOException
    {
        List<List<String>> rawProductData = FileStorageUtil.readMultiColumnFile("data/Products.txt");

        for (List<String> rowData : rawProductData)
        {
            String productId = rowData.get(0);
            String name = rowData.get(1);
            String brand = rowData.get(2);
            String description = rowData.get(3);
            double price = Double.parseDouble(rowData.get(4));
            double memberPrice = Double.parseDouble(rowData.get(5));
            int quantity = Integer.parseInt(rowData.get(6));
            String categoryName = rowData.get(7);
            String subcategoryName = rowData.get(8);
            boolean isFood = Boolean.parseBoolean(rowData.get(9));

            Subcategory sub = findSubcategory(subcategoryName, categoryName);
            if (sub == null) {
                throw new IllegalStateException(
                        "Unknown subcategory '" + subcategoryName +
                                "' for category '" + categoryName + "'.");
            }

            FoodInfo foodInfo = null;
            if (isFood)
            {
                if (rowData.size() < 14)
                {
                    throw new IllegalArgumentException("Invalid row data - FoodInfo could be missing!");
                }
                String expiry = rowData.get(10);
                String ingredients = rowData.get(11);
                String storage = rowData.get(12);
                String allergenInfo = rowData.get(13);
                foodInfo = new FoodInfo(expiry, ingredients, storage, allergenInfo);
            }

            Product product = new Product(
                    productId, name, brand, description,
                    price, memberPrice, quantity, sub, isFood, foodInfo
            );

            productList.add(product);

        }
    }

    /**
     * Method to search for category object.
     *
     * @param categoryName String value of the category name to be found.
     * @return the matching Category object.
     */
    private Category findCategory(String categoryName)
    {
        for (Category category : categoryList)
        {
            if (category.getCategoryName().equalsIgnoreCase(categoryName))
            {
                return category;
            }
        }
        return null;
    }

    /**
     * Method to find subcategory given the subcategory name.
     *
     * @param subcategoryName String value of the subcategory name that needs to be found.
     * @param categoryName String value of the category name that the subcategory belongs to.
     * @return Subcategory object if found else null.
     */
    public Subcategory findSubcategory(String subcategoryName, String categoryName)
    {
        for (Subcategory subcategory : subcategoryList)
        {
            if (subcategory.getSubCatName().equalsIgnoreCase(subcategoryName)
            && subcategory.getCategory().getCategoryName().equalsIgnoreCase(categoryName))
            {
                return subcategory;
            }
        }
        return null;
    }


    /**
     * Method to get list of products.
     * Returns the list of product objects.
     * @return List<Product> - the loaded list of product objects.
     */
    public List<Product> getProductList()
    {
        return productList;
    }

    /**
     * The method is used to find a product using its product id.
     *
     * @param productId the id of the product that is being asked for by client class.
     * @return The product object seeked.
     */
    public Product getProduct(String productId)
    {
        for (Product product : productList)
        {
            if (product.getProductId().equals(productId))
            {
                return product;
            }
        }
        return null;
    }
    /**
     * Method to get list of categories.
     * @return List of Category objects.
     */
    public List<Category> getCategoryList() {
        return categoryList;
    }

    /**
     * Method to get list of subcategories.
     * @return List of SubCategory objects.
     */
    public List<Subcategory> getSubcategoryList() {
        return subcategoryList;
    }

    public void updateInventory(String productFilepath, Order order) throws IOException {
        if (order == null || order.getOrderItems() == null) {
            return;
        }

        List<List<String>> rows = FileStorageUtil.readMultiColumnFile(productFilepath);
        List<String> lines = new ArrayList<>();

        lines.add("product_id,name,brand,description,price,member_price,quantity,category_name,"
                + "subcategory_name,is_food,expiry_date,ingredient_list,storage_instructions,allergen_info");

        for (List<String> row : rows)
        {
            if (row.isEmpty())
            {
                continue;
            }

            String productId = row.get(0).trim();

            for (CartItem item : order.getOrderItems())
            {
                if (item.getProduct().getProductId().equalsIgnoreCase(productId))
                {
                    int currentQty = Integer.parseInt(row.get(6).trim());
                    int newQty = Math.max(currentQty - item.getQuantity(), 0);

                    row.set(6, String.valueOf(newQty));
                    item.getProduct().setQuantity(newQty);
                }
            }

            lines.add(String.join(",", row));
        }

        FileStorageUtil.writeToFile(productFilepath, lines);
    }

    /**
     * Validates that expiry date is in correct format and not expired or unreasonably far in the future.
     * @param expiryDate String date entered by user (expected format: YYYY-MM-DD)
     */
    public boolean validateExpiryDate(String expiryDate)
    {
        try {
            LocalDate date = LocalDate.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();

            if (date.isBefore(today)) {
                return false;
            }
            return !date.isAfter(today.plusYears(2));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiry date format! Please use YYYY-MM-DD");
        }
    }
}