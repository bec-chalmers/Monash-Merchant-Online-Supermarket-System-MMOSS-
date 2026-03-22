package entity;

/** An entity class which represents
 * a Promotion that can be applied to an Order.
 * It stores details like promo code, description,
 * fixed or percentage discount, validity period,
 * promotion type, and any conditions.
 *
 * @author Rebecca Chalmers
 * @version 1.0
 */
public class Promotion
{
    private String code;
    private String description;
    private int amountOff;
    private int percentageOff;
    private String validPeriod;
    private String type;
    private String conditions;

    /**
     * Non-Default Constructor for Promotion class.
     * Creates a new Promotion with all of its details.
     *
     * @param code           The unique promotion code.
     * @param description    The human-readable description.
     * @param amountOff      The fixed discount amount.
     * @param percentageOff  The percentage discount (0–100).
     * @param validPeriod    The validity period description.
     * @param type           The promotion type/category.
     * @param conditions     The conditions/restrictions text.
     */
    public Promotion(String code,
                     String description,
                     int amountOff,
                     int percentageOff,
                     String validPeriod,
                     String type,
                     String conditions)
    {
        this.code = code;
        this.description = description;
        this.amountOff = amountOff;
        this.percentageOff = percentageOff;
        this.validPeriod = validPeriod;
        this.type = type;
        this.conditions = conditions;
    }

    /**
     * Accessor Method for Promotion to get the fixed amount off.
     *
     * @return int  The fixed discount amount.
     */
    public int getAmountOff()
    {
        return amountOff;
    }

    /**
     * Accessor Method for Promotion to get the code.
     *
     * @return String  The promotion code.
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Accessor Method for Promotion to get the conditions.
     *
     * @return String  The conditions/restrictions text.
     */
    public String getConditions()
    {
        return conditions;
    }

    /**
     * Accessor Method for Promotion to get the description.
     *
     * @return String  The promotion description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Accessor Method for Promotion to get the percentage off.
     *
     * @return int  The percentage discount (0–100).
     */
    public int getPercentageOff()
    {
        return percentageOff;
    }

    /**
     * Accessor Method for Promotion to get the type.
     *
     * @return String  The promotion type/category.
     */
    public String getType()
    {
        return type;
    }

    /**
     * Accessor Method for Promotion to get the valid period text.
     *
     * @return String  The validity period description.
     */
    public String getValidPeriod()
    {
        return validPeriod;
    }

    /**
     * Mutator Method for Promotion to set the fixed amount off.
     *
     * @param amountOff  The fixed discount amount.
     */
    public void setAmountOff(int amountOff)
    {
        this.amountOff = amountOff;
    }

    /**
     * Mutator Method for Promotion to set the code.
     *
     * @param code  The new promotion code.
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * Mutator Method for Promotion to set the conditions.
     *
     * @param conditions  The new conditions/restrictions text.
     */
    public void setConditions(String conditions)
    {
        this.conditions = conditions;
    }

    /**
     * Mutator Method for Promotion to set the description.
     *
     * @param description  The new description text.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Mutator Method for Promotion to set the percentage off.
     *
     * @param percentageOff  The percentage discount (0–100).
     */
    public void setPercentageOff(int percentageOff)
    {
        this.percentageOff = percentageOff;
    }

    /**
     * Mutator Method for Promotion to set the type.
     *
     * @param type  The new promotion type/category.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Mutator Method for Promotion to set the valid period text.
     *
     * @param validPeriod  The new validity period description.
     */
    public void setValidPeriod(String validPeriod)
    {
        this.validPeriod = validPeriod;
    }
}