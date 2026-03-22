import boundry.AuthView;
import controllers.AuthenticationController;
import controllers.PickupController;
import controllers.ProductController;
import controllers.PromoController;

public class MMOSSApp
{
    public static void main(String[] args)
    {
        String pathToCustomerStorage = "data/Customer.txt";
        String pathToPickupStoreStorage = "data/PickupStore.txt";

        AuthenticationController authController = new AuthenticationController(pathToCustomerStorage);
        PickupController pickupController = new PickupController(pathToPickupStoreStorage);
        ProductController productController = new ProductController();
        PromoController promoController = new PromoController();

        AuthView authView = new AuthView(authController, productController, pickupController, promoController);
        authView.start();
    }
}