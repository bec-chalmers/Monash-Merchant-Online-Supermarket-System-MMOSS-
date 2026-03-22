package entity;
import java.util.ArrayList;
import java.time.LocalDate;

/** An entity class Customer which is a
 * subclass of User entity class. It comprises
 * the additional information for Customer user.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class Customer extends User
{
    private String dateOfBirth;
    private String gender;
    private Address address;
    private boolean isStudent;
    private double balance;
    private ArrayList<Order> orderHistory;
    private ArrayList<VIPMembership> membershipHistory;
    private Cart cart;

    /**
     * Default Constructor for User class.
     *
     */
    public Customer()
    {
        super("", "", "", "", ""); // empty defaults
    }

    /**
     * Non-Default Constructor for User class.
     *
     * @param   email           The customer email.
     * @param   password        The customer password.
     * @param   firstName       The customer first name.
     * @param   lastName        The customer last name.
     * @param   mobile          The customer mobile number.
     * @param   dateOfBirth     The customer date of birth.
     * @param   gender          The customer gender.
     * @param   address         The customer address.
     * @param   isStudent        The customer student status.
     * @param   balance          The customer balance.
     * @param   orderHistory     The list of order history.
     * @param   membershipHistory The list of membership history.
     * @param   cart              The cart object of customer.
     */
    public Customer(String email, String password, String firstName, String lastName, String mobile,
                    String dateOfBirth, String gender, Address address,
                    boolean isStudent, double balance,
                    ArrayList<Order> orderHistory,
                    ArrayList<VIPMembership> membershipHistory,
                    Cart cart)
    {
        super(email, password, firstName, lastName, mobile);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.isStudent = isStudent;
        this.balance = balance;
        this.orderHistory = new ArrayList<>();
        this.membershipHistory = new ArrayList<>();
        this.cart = cart;
    }

    /**
     * Accessor Method of Customer to get the address.
     *
     * @return   String    The address as String.
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * Accessor Method of Customer to get the balance.
     *
     * @return   double    The balance as double.
     */
    public double getBalance()
    {
        return balance;
    }

    /**
     * Accessor Method of Customer to get the cart.
     *
     * @return   Cart   The cart object.
     */
    public Cart getCart()
    {
        return cart;
    }

    /**
     * Accessor Method of Customer to get date of birth.
     *
     * @return   String    The date of birth as String.
     */
    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    /**
     * Accessor Method of Customer to get the gender.
     *
     * @return   String    The gender as String.
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * Accessor Method of Customer to get the order history.
     *
     * @return   ArrayList<Order>   An Arraylist of order history
     *                              objects
     */
    public ArrayList<Order> getOrderHistory()
    {
        return orderHistory;
    }

    /**
     * Accessor Method of Customer to get the membership history.
     *
     * @return   ArrayList<VIPMembership>   An Arraylist of membership history
     *                                      objects
     */
    public ArrayList<VIPMembership> getMembershipHistory()
    {
        return membershipHistory;
    }

    /**
     * Accessor Method of Customer to get the student status.
     * True if student, false if staff.
     *
     * @return   boolean    The student status as true/false.
     */
    public boolean isStudent()
    {
        return isStudent;
    }

    /**
     * Mutator Method of Customer to set the address.
     *
     * @param   address    The address as String.
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Mutator Method of Customer to set the balance.
     *
     * @param   balance    The balance as double.
     */
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    /**
     * Mutator Method of Customer to set the cart.
     *
     * @param   cart   The cart object.
     */
    public void setCart(Cart cart)
    {
        this.cart = cart;
    }

    /**
     * Mutator Method of Customer to set date of birth.
     *
     * @param   dateOfBirth    The date of birth as String.
     */
    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Mutator Method of Customer to set the gender.
     *
     * @param   gender    The gender as String.
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    /**
     * Mutator Method of Customer to set the student status.
     * True if student, false if staff.
     *
     * @param   isStudent    The student status as true/false.
     */
    public void setIsStudent(boolean isStudent)
    {
        this.isStudent = isStudent;
    }

    /**
     * Mutator Method of Customer to set the order history.
     *
     * @param   orderHistory   An Arraylist of order history
     *                              objects
     */
    public void setOrderHistory(ArrayList<Order> orderHistory)
    {
        this.orderHistory = orderHistory;
    }

    /**
     * Mutator Method of Customer to set the membership history.
     *
     * @param   membershipHistory   An Arraylist of membership history
     *                                      objects
     */
    public void setMembershipHistory(ArrayList<VIPMembership> membershipHistory)
    {
        this.membershipHistory = membershipHistory;
    }
}