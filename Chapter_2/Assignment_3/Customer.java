package Chapter_2.Assignment_3;

public class Customer {
    private Integer customerID;
    private String companyName;
    private String contactName;
    private String countryName;

    public Integer getCustomerID() {
        return customerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return "Customer [customerID=" + customerID + ", companyName=" + companyName + ", contactName=" + contactName
                + ", countryName=" + countryName + "]";
    }
}