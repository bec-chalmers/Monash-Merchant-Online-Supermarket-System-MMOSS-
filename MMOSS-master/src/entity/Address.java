package entity;

/** An entity class Address which holds the
 * details of the Address of a customer.
 *
 * @author Arshi Siddiqui Promiti
 * @version 1.0
 */
public class Address
{
    private String buildingNumber;
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String state;
    private String postCode;

    /**
     * Default Constructor
     */
    public Address()
    {

    }

    /**
     * Non-Default Constructor for Address class.
     *
     * @param   buildingNumber      Building Number of Customer address
     * @param   streetNumber        Street Number of Customer address
     * @param   streetName          Street Name of Customer address
     * @param   suburb              Suburb name of Customer address
     * @param   state               State of Customer address
     * @param   postCode            Post code of Customer address
     */
    public Address(String buildingNumber, String streetNumber, String streetName, String suburb, String state, String postCode)
    {
        this.buildingNumber = buildingNumber;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.suburb = suburb;
        this.state = state;
        this.postCode = postCode;
    }

    /**
     * Accessor Method of Address to get the building number.
     *
     * @return   String    The building number as String.
     */
    public String getBuildingNumber()
    {
        return buildingNumber;
    }

    /**
     * Accessor Method of Address to get the post code.
     *
     * @return   String    The post code as String.
     */
    public String getPostCode()
    {
        return postCode;
    }

    /**
     * Accessor Method of Address to get the street name.
     *
     * @return   String    The street name as String.
     */
    public String getStreetName()
    {
        return streetName;
    }

    /**
     * Accessor Method of Address to get the street number.
     *
     * @return   String    The street number as String.
     */
    public String getStreetNumber() { return streetNumber; }

    /**
     * Accessor Method of Address to get the suburb.
     *
     * @return   String    The suburb as String.
     */
    public String getSuburb()
    {
        return suburb;
    }

    /**
     * Accessor Method of Address to get the state.
     *
     * @return   String    The state as String.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Mutator Method of Address to set the building number.
     *
     * @param   buildingNumber    The building number as String.
     */
    public void setBuildingNumber(String buildingNumber)
    {
        this.buildingNumber = buildingNumber;
    }

    /**
     * Mutator Method of Address to set the post code.
     *
     * @param   postCode    The post code as String.
     */
    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    /**
     * Mutator Method of Address to set the street name.
     *
     * @param   streetName    The street name as String.
     */
    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    /**
     * Mutator Method of Address to set the street number.
     *
     * @param   streetNumber    The street number as String.
     */
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }

    /**
     * Mutator Method of Address to set the suburb.
     *
     * @param   suburb    The suburb as String.
     */
    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    /**
     * Mutator Method of Address to set the state.
     *
     * @param   state    The state as String.
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * toString method to return the address details.
     *
     * @return   String    The address details as a String.
     */
    public String toString()
    {
        // checks if the address includes a building number
        if (buildingNumber != null && !buildingNumber.isEmpty()) {
            return buildingNumber + "/" + streetNumber + " " + streetName + ", " + suburb + " " + state + " " + postCode;
        }

        // returns address as a string
        return streetNumber + " " + streetName + ", " + suburb + " " + state + " " + postCode;
    }
}
