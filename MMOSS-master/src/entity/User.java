package entity;

/** An entity class User which contains
 * the user details common to both
 * Customer and Admin. User is a superclass.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class User
{
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String mobileNumber;

    /**
     * Non-Default Constructor for User class.
     *
     * @param   email           The user email.
     * @param   password        The user password.
     * @param   firstName       The user first name.
     * @param   lastName        The user last name.
     * @param   mobileNumber    The user mobile number.
     */
    public User(String email, String password, String firstName, String lastName, String mobileNumber)
    {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
    }

    /**
     * Accessor Method for User to get email.
     *
     * @return   String    The user email as String.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Mutator Method for User to set email.
     *
     * @param   email    The user email as String.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Accessor Method for User to get password.
     *
     * @return   String    The user password as String.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Mutator Method for User to set password.
     *
     * @param   password    The user password as String.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Accessor Method for User to get first name.
     *
     * @return   String    The user first name as String.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Mutator Method for User to get first name.
     *
     * @param   firstName    The user first name as String.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Accessor Method for User to get last name.
     *
     * @return   String    The user last name as String.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Mutator Method for User to get last name.
     *
     * @param   lastName    The user last name as String.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Accessor Method for User to get mobile number.
     *
     * @return   String    The user mobile number as String.
     */
    public String getMobileNumber()
    {
        return mobileNumber;
    }

    /**
     * Mutator Method for User to get mobile number.
     *
     * @param   mobileNumber    The user mobile number as String.
     */
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }
}
