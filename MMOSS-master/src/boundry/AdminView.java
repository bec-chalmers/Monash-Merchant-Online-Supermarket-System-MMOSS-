package boundry;
import controllers.ProductController;
import entity.*;
import util.BannerUtil;
import util.ErrorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the boundary or the user interface logic for the admin to interact with the system.
 * Focuses on getting user input and delegates the actions(business logic) to the ProductController.
 *
 * @author Yapa Jayasinghe
 * @version 1.0
 */
public class AdminView
{
    private final ProductController productController;
    private final Admin admin;
    Scanner scanner = new Scanner(System.in);

    /**
     * Creates an instance of Admin view for the admin to interact with.
     * @param user User Obj passed in after authentication / authenticated user .
     * @param productController ProductController Obj to handle product related buisness logic.
     */
    public AdminView(User user, ProductController productController)
    {
        this.productController = productController;
        this.admin = (Admin) user;
    }

    /**\
     * Shows the admin menu options for the admin to navigate.
     * loops continuously until the user selects the logout option.
     */
    public void showAdminMenu()
    {
        while (true)
        {
            BannerUtil.printHeader();
            BannerUtil.printSubheader("Admin Options");
            System.out.println("1) View Profile");
            System.out.println("2) Add Product");
            System.out.println("3) Edit Product");
            System.out.println("4) Delete Product");
            System.out.println("5) Logout");

            System.out.print("Enter your choice (select 1-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1":
                    viewProfile();
                    break;
                case "2":
                    addProduct();
                    break;
                case "3":
                    editProduct();
                    break;
                case "4":
                    deleteProduct();
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }

        }
    }

    /**
     * Prompts the admin for product related data to add a new product.
     * Handles input validation in the process.
     */
    private void addProduct()
    {
        BannerUtil.printSubheader("Add New Product");

        Object[] values = collectProductAttributes(false, null, null);
        // for when user enters 0
        if (values ==  null)
        {
            return;
        }
        try
        {
            productController.addNewProduct(
                    (String) values[0],   // name
                    (String) values[1],   // brand
                    (String) values[2],   // description
                    (Double) values[3],   // price
                    (Double) values[4],   // member price
                    (Integer) values[5],  // quantity
                    (String) values[6],   // category name
                    (String) values[12],   // subcategory name
                    (boolean) values[7],  // isFood flag
                    (String)values[8], (String)values[9], (String)values[10], (String)values[11] // food details
            );
            displayProducts("Product added successfully!!",productController.getProductList());
            System.out.print("Continue adding products? (Y/N): ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("Y"))
            {
                addProduct();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error saving product " + e.getMessage());
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid numeric input!");
        }

    }

    /**
     * Collects all the product attributes from the user / admin.
     * Used when adding and editing products.
     *
     * @param isEdit boolean check for whether this method is being used in the process of editing or adding.
     * @param existingSelectedCategory Category of a product to be edited.
     * @param existingSelectedSubcategory Subcategory of a product to be edited.
     * @return Object obj containing user input for each product attribute.
     */
    private Object[] collectProductAttributes(boolean isEdit, Category existingSelectedCategory, Subcategory existingSelectedSubcategory)
    {
        Object[] userInputValues = new Object[13];

        userInputValues[0] = promptNonEmptyString("Enter Product Name", isEdit);
        if (userInputValues[0].equals("0")) return null;

        userInputValues[1] = promptNonEmptyString("Enter Product Brand", isEdit);
        if (userInputValues[1].equals("0")) return null;

        userInputValues[2] = promptNonEmptyString("Enter Product Description", isEdit);
        if (userInputValues[2].equals("0")) return null;

        userInputValues[3]  = promptDouble("Enter Product Price (AUD)", isEdit);
        if ((double)userInputValues[3]  == 0) return null;

        userInputValues[4] = promptDouble("Enter Product Member Price", isEdit);
        if ((double)userInputValues[4] == 0) return null;

        userInputValues[5] = promptInt("Enter Product Quantity", isEdit);
        if ((int)userInputValues[5] == 0) return null;

        // selecting a category
        Category category = existingSelectedCategory != null ? existingSelectedCategory : promptCategory(isEdit);
        if (category == null && !isEdit) return null;
        userInputValues[6] = category != null ? category.getCategoryName() : "";

        // if the product is a food item
        boolean isFood = category != null && category.getCategoryName().equalsIgnoreCase("Food");
        userInputValues[7] = isFood;
        if (isFood)
        {
            userInputValues[8] = promptNonEmptyString("Enter expiry date |YYYY-MM-DD|", isEdit);
            if (userInputValues[8].equals("0")) return null;

            userInputValues[9] = promptNonEmptyString("Enter ingredient list", isEdit);
            if (userInputValues[9].equals("0")) return null;

            userInputValues[10] = promptNonEmptyString("Enter storage instructions", isEdit);
            if (userInputValues[10].equals("0")) return null;

            userInputValues[11] = promptNonEmptyString("Enter allergen info", isEdit);
            if (userInputValues[11].equals("0")) return null;
        }

        // selecting subcategory
        Subcategory sub = existingSelectedSubcategory != null ? existingSelectedSubcategory : promptSubcategory(category, isEdit);
        userInputValues[12] = sub != null ? sub.getSubCatName() : "";

        return userInputValues;

    }


    /**
     * Edits a selected existing product by its id.
     * Displays the current details of the selected product before prompting for the updates.
     */
    public void  editProduct()
    {
        displayProducts("Available Products", productController.getProductList());
        String selectedProductId = promptNonEmptyString("Enter Product ID", false);
        if (selectedProductId.equals("0")) return;

        // getting the product from controller
        Product selectedProduct = productController.getProduct(selectedProductId);

        // display of current selected product detail
        List<Product> prodList = new ArrayList<>();
        prodList.add(selectedProduct);
        displayProducts("Edit - "+selectedProduct.getName(), prodList);

        // getting the new updated values
        Object[] updatedValues = collectProductAttributes(true, selectedProduct.getSub().getCategory(), selectedProduct.getSub());
        if (updatedValues == null) return;

        try {
            productController.updateProduct(
                    selectedProductId,
                    (String) updatedValues[0],     // name
                    (String) updatedValues[1],     // brand
                    (String) updatedValues[2],     // description
                    (Double) updatedValues[3],     // price (-1 if skipped)
                    (Double) updatedValues[4],     // member price (-1 if skipped)
                    (Integer) updatedValues[5],    // quantity (-1 if skipped)
                    (String) updatedValues[6],     // category name ("" if skipped)
                    (String) updatedValues[12],    // subcategory name ("" if skipped)
                    (boolean) updatedValues[7],    // isFood
                    (String) updatedValues[8],     // expiry
                    (String) updatedValues[9],     // ingredients
                    (String) updatedValues[10],    // storage
                    (String) updatedValues[11]     // allergens
            );
            displayProducts("Product successfully edited!", productController.getProductList());
            System.out.print("Continue editing products? (Y/N): ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("Y"))
            {
                editProduct();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error saving product " + e.getMessage());
        }
    }


    /**
     * Allows the admin to delete product by chosen id after a confirmation.
     * The remaining products are shown for confirmation of delete afterward.
     */
    private void deleteProduct()
    {
        displayProducts("Delete Product", productController.getProductList());
        String selectedProductId = promptNonEmptyString("Enter Product ID to delete", false);
        if (selectedProductId.equals("0")) return;
        BannerUtil.printHeader();
        BannerUtil.printSubheader("Delete Product");
        String confirm = promptNonEmptyString("Are you sure you want to delete this product? (Y/N): ", false);
        if (confirm.equalsIgnoreCase("Y"))
        {
            try {
                productController.removeProduct(selectedProductId);
                displayProducts("Product successfully Deleted!", productController.getProductList());
                System.out.print("Continue to delete products? (Y/N): ");
                String choice = scanner.nextLine().trim();
                if (choice.equalsIgnoreCase("Y")) {
                    deleteProduct();
                }
            } catch (IOException e) {
                System.out.println("Error deleting product " + e.getMessage());
            }
        }
    }

    /**
     * Prompts the admin to enter a category selection.
     *
     * @param isEdit boolean to check if the selection happens during an edit process or not.
     * @return Category Obj selected by the admin.
     */
    private Category promptCategory(boolean isEdit)
    {
        List<Category> categories = productController.getCategoryList();
        BannerUtil.printSubheader("Category Selection");

        int i=1;
        for (Category category : categories)
        {
            System.out.println(i+") "+category.getCategoryName());
            i++;
        }

        int choice;
        while (true)
        {
            choice = promptInt("Select Category Option", isEdit);
            if (choice == 0)
            {
                return null;
            }
            if (choice >=1 && choice <= categories.size())
            {
                return categories.get(choice - 1);
            }
            System.out.println("Invalid Category Option");
        }
    }

    /**
     * Prompts the admin to select a subcategory under a selected category.
     *
     * @param selectedCategory Category Obj selected by the admin.
     * @param isEdit boolean to check if the selection happens during an edit process or not.
     * @return SubCategory Obj selected by the admin.
     */
    private Subcategory promptSubcategory(Category selectedCategory, boolean isEdit)
    {
        List<Subcategory> subCategories = productController.getSubcategoryList();
        List<Subcategory> filteredSubcategoryList = new ArrayList<>();

        for (Subcategory subcategory : subCategories)
        {
            if (subcategory.getCategory()!=null
            && subcategory.getCategory().getCategoryName().equalsIgnoreCase(selectedCategory.getCategoryName())){
                filteredSubcategoryList.add(subcategory);
            }
        }

        BannerUtil.printSubheader("Subcategory Selection");

        int i=1;
        for (Subcategory subcategory : filteredSubcategoryList)
        {
            System.out.println(i+") "+subcategory.getSubCatName());
            i++;
        }

        int choice;
        while (true)
        {
            choice = promptInt("Select Subcategory Option", isEdit);
            if (choice == 0)
            {
                return null;
            }
            if (choice >=1 && choice <= filteredSubcategoryList.size())
            {
                return filteredSubcategoryList.get(choice - 1);
            }
        }

    }

    /**
     * Prompts for a non-empty string input with the optional cancel or skip prompt messages.
     *
     * @param prompt String part of the prompt to be added.
     * @param isEdit boolean to check if the selection happens during an edit process or not.
     * @return String the string value entered by the user, "0" if cancelled, or empty string if skipped.
     */
    private String promptNonEmptyString(String prompt, boolean isEdit)
    {
        while (true)
        {
            System.out.print(prompt + (isEdit ? " (press enter to skip) : " : " (0 - cancel) : "));
            String input = scanner.nextLine().trim();
            if (isEdit && input.isEmpty()) return ""; // to skip
            if (!isEdit && input.equals("0")) return "0"; // to cancel
            if (!isEdit && input.isEmpty()) {
                ErrorUtil.showErrorScreen("Should not be empty!");
                continue;
            }
            // format validation for expiry date
            if (prompt.toLowerCase().contains("expiry") && !input.matches("\\d{4}-\\d{2}-\\d{2}"))
            {
                ErrorUtil.showErrorScreen("Invalid format! Please enter date as YYYY-MM-DD (e.g., 2025-12-31)");
                continue;
            }
            if (prompt.toLowerCase().contains("expiry") && !productController.validateExpiryDate(input))
            {
                ErrorUtil.showErrorScreen("Invalid expiry date!(expired already/too ahead!)");
                continue;
            }
            return input;
        }
    }

    /**
     *
     * @param prompt String part of the prompt to be added.
     * @param isEdit boolean to check if the selection happens during an edit process or not.
     * @return a valid double, 0 to cancel, or -1 if skipped.
     */
    private double  promptDouble(String prompt, boolean isEdit)
    {
        while (true)
        {
            System.out.print(prompt + (isEdit ? " (press enter to skip) : " : " (0 - cancel) : "));
            String input = scanner.nextLine().trim();

            // when used for editing - to skip
            if (isEdit && input.isEmpty())
            {
                return -1;
            }
            if (!isEdit && input.equals("0"))
            {
                return 0; // to cancel
            }
            if (!isEdit && input.isEmpty()) {
                System.out.println("Should not be empty!");
                continue;
            }
            try {
                double value = Double.parseDouble(input);
                if (value > 0) {
                    return value;
                }
                ErrorUtil.showErrorScreen("Please enter a number greater than 0.");
            } catch (NumberFormatException e)
            {
                ErrorUtil.showErrorScreen("Invalid number format!");
            }

        }
    }

    /**
     * Prompts for a non-empty integer input with the optional cancel or skip prompt messages.
     *
     * @param prompt String part of the prompt to be added.
     * @param isEdit boolean to check if the selection happens during an edit process or not.
     * @return an integer value, 0 to cancel, or -1 if skipped.
     */
    private  int promptInt(String prompt,  boolean isEdit)
    {
        while (true)
        {
            System.out.print(prompt + (isEdit ? " (press enter to skip) : " : " (0 - cancel) : "));
            String input = scanner.nextLine().trim();

            if (isEdit && input.isEmpty()) return -1;
            if (!isEdit && input.equals("0")) return 0;

            if (input.isEmpty()) {
                System.out.println("Should not be empty!");
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value >= 0){
                    return value;
                }
                ErrorUtil.showErrorScreen("Quantity cannot be negative.");
            }
            catch (NumberFormatException e)
            {
                ErrorUtil.showErrorScreen("Invalid number format!");
            }
        }
    }

    /**
     * Method to display product/products in tabular format.
     *
     * @param msg String message to be shown in the subheader.
     * @param productList List<Product> to be displayed as a table.
     */
    public void displayProducts(String msg, List<Product> productList)
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader(msg);
        System.out.println();

        System.out.printf("%-6s %-20s %-15s %-10s %-10s %-10s %-15s%n",
                "No.", "Name", "Brand", "Price($)", "Qty", "Member($)", "Category");
        System.out.println("-------------------------------------------------------------------------------");

        for (Product p : productList)
        {
            System.out.printf("%-6s %-20s %-15s %-10.2f %-10d %-10.2f %-15s%n",
                    p.getProductId(),
                    p.getName(),
                    p.getBrand(),
                    p.getPrice(),
                    p.getQuantity(),
                    p.getMemberPrice(),
                    p.getSub() != null ? p.getSub().getSubCatName() : "N/A");
        }

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println();
    }

    /**
     * To view profile details of admin.
     */
    public void viewProfile()
    {
        BannerUtil.printHeader();
        BannerUtil.printSubheader(admin.getFirstName()+" "+admin.getLastName());

        System.out.println("First Name: "+admin.getFirstName());
        System.out.println("Last Name: "+admin.getLastName());
        System.out.println("Phone Number: "+admin.getMobileNumber());
        System.out.println();
        System.out.println("Email: "+admin.getEmail());
        System.out.println("Password: "+admin.getPassword());

        System.out.print("Enter 0 to go back : ");
        while (true)
        {
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return;
            }
            System.out.print("Invalid input! Please enter 0 to go back : ");
        }
    }
}
