package entity;
import java.time.LocalDate;

/** An entity class VIPMembership which contains
 * the VIP membership details for a customer
 * who is a member.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.1
 */
public class  VIPMembership
{
    private static final double VIP_PRICE_PER_YEAR = 20.0; // at AUD 20/year
    private double membershipPrice;
    private int yearsPurchased;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private boolean activeStatus;
    private String benefits;
    private LocalDate cancellationDate;
    private String type; // Purchase, renew, cancelled - types

    /**
     * Default Constructor for VIPMembership class.
     *
     */
    public  VIPMembership()
    {

    }

    /**
     * Non-Default Constructor for VIPMembership class.
     * This is used to create VIP membership when customer
     * purchases or renews and needs the date to be set
     * inside constructor.
     * It sets the time of purchase automatically.
     *
     * @param   yearsPurchased      The number of years of membership.
     * @param   type      The type of membership - Purchase, Renewal.
     */
    public VIPMembership(int yearsPurchased, String type)
    {
        this.membershipPrice = yearsPurchased * VIP_PRICE_PER_YEAR;
        this.yearsPurchased = yearsPurchased;
        this.startDate = LocalDate.now(); // the day it will start
        this.expiryDate = startDate.plusYears(yearsPurchased);
        this.activeStatus = true;
        this.benefits = "Special Member prices, free delivery and pickup discount for Monash students.";
        this.type = type;
    }

    /**
     * Non-Default Constructor for VIPMembership Class.
     * This is used when the membership details needs to be
     * loaded from the csv file and objects needs to be created using
     * the already given startDate, expiryDate and status.
     *
     * @param   type      The type of membership - Purchase, Renewal.
     * @param   startDate    The start date of the membership.
     * @param   expiryDate   The expiry date of the membership.
     * @param   isActive    The active status of the membership.
     */
    public VIPMembership(String type, LocalDate startDate, LocalDate expiryDate, boolean isActive)
    {
        this.type = type;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.activeStatus = isActive;
    }

    /**
     * The method used to set the cancellation status
     * of a membership with the cancellation date.
     *
     * @param   cancellationDate     The date of cancellation.
     */
    public void cancelMembership(LocalDate cancellationDate)
    {
        if (isActive())
        {
            this.cancellationDate = cancellationDate;
            this.activeStatus = false;
            this.type = "Cancelled";
        }
    }

    /**
     * The method used to extend the expiration
     * date of the membership by the given years.
     *
     * @param   years     The no of years to extend.
     */
    public void extendExpiry(int years)
    {
        this.expiryDate = this.expiryDate.plusYears(years);
    }

    /**
     * Accessor Method of VIPMembership to get the
     * active status of membership.
     *
     * @return   boolean    The active status as boolean.
     */
    public boolean getActiveStatus()
    {
        return activeStatus;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * benefits of membership.
     *
     * @return   String    The membership benefits as String.
     */
    public String getBenefits()
    {
        return benefits;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * cancellation date of membership.
     *
     * @return   LocalDate    The membership cancellation date as LocalDate.
     */
    public LocalDate getCancellationDate()
    {
        return cancellationDate;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * expiry date of membership.
     *
     * @return   LocalDate    The membership expiry date as LocalDate.
     */
    public LocalDate getExpiryDate()
    {
        return expiryDate;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * price of membership.
     *
     * @return   double    The membership price as double.
     */
    public double getMembershipPrice()
    {
        return membershipPrice;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * start date of membership.
     *
     * @return   LocalDate    The membership start date as LocalDate.
     */
    public LocalDate getStartDate()
    {
        return startDate;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * type of membership.
     *
     * @return   String    The membership type as String.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * cost per year for VIP membership.
     *
     * @return   double    The VIP cost per year as double.
     */
    public static double getVipCostPerYear() {
        return VIP_PRICE_PER_YEAR;
    }

    /**
     * Accessor Method of VIPMembership to get the
     * number of years purchased for membership.
     *
     * @return   int    The no of years as double.
     */
    public int getYearsPurchased()
    {
        return yearsPurchased;
    }

    /**
     * The method used to return true if the membership
     * is active for the user.
     *
     * @return   boolean     True if active, false otherwise.
     */
    public boolean isActive()
    {
        return activeStatus && (cancellationDate == null) && (LocalDate.now().isBefore(expiryDate));
    }

    /**
     * Mutator Method of VIPMembership to set the
     * active status of membership.
     *
     * @param   activeStatus    The active status as boolean.
     */
    public void setActiveStatus(boolean activeStatus)
    {
        this.activeStatus = activeStatus;
    }

    /**
     * Mutator Method of VIPMembership to set the
     * cancellation date of membership.
     *
     * @param   cancellationDate    The membership cancellation date as LocalDate.
     */
    public void setCancellationDate(LocalDate cancellationDate)
    {
        this.cancellationDate = cancellationDate;
    }

    /**
     * Mutator Method of VIPMembership to set the
     * expiry date of membership.
     *
     * @param   expiryDate    The membership expiry date as LocalDate.
     */
    public void setExpiryDate(LocalDate expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    /**
     * Mutator Method of VIPMembership to set the
     * start date of membership.
     *
     * @param   startDate    The membership start date as LocalDate.
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Mutator Method of VIPMembership to set the
     * type of membership.
     *
     * @param   type    The membership type as String.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Mutator Method of VIPMembership to set the
     * number of years purchased for membership.
     *
     * @param   yearsPurchased    The no of years as int.
     */
    public void setYearsPurchased(int yearsPurchased)
    {
        this.yearsPurchased = yearsPurchased;
    }
}