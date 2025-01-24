package Chapter_2.Assignment_3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class CustomerSearch {

    private List<Customer> customers = new ArrayList<>();

    public CustomerSearch(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> searchByCountry(String countryName) {
        return customers.stream()
                .filter(customer -> customer.getCountryName().contains(countryName))
                .sorted((firstCustomer, secondCustomer) -> firstCustomer.getCustomerID()
                        .compareTo(secondCustomer.getCustomerID()))
                .collect(Collectors.toList());
    }

    public List<Customer> searchByCompanyName(String companyName) {
        return customers.stream()
                .filter(customer -> customer.getCompanyName().contains(companyName))
                .sorted((firstCustomer, secondCustomer) -> firstCustomer.getCustomerID()
                        .compareTo(secondCustomer.getCustomerID()))
                .collect(Collectors.toList());
    }

    public List<Customer> searchByContact(String contactName) {
        return customers.stream()
                .filter(customer -> customer.getContactName().contains(contactName))
                .sorted((firstCustomer, secondCustomer) -> firstCustomer.getCustomerID()
                        .compareTo(secondCustomer.getCustomerID()))
                .collect(Collectors.toList());
    }

    public String exportToCSV(List<Customer> customerList) {
        StringBuilder csvContent = new StringBuilder();
        customerList.forEach(customer -> {
            csvContent.append(customer.getCustomerID()).append(",")
                    .append(customer.getCompanyName()).append(",")
                    .append(customer.getContactName()).append(",")
                    .append(customer.getCountryName()).append("\n");
        });
        return csvContent.toString();
    }
}
