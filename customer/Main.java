package customer;

import customer.modal.Customer;
import customer.service.CustomerWalletService;

public class Main {
    public static void main(String[] args) {
        Customer customer=new Customer("Demo","Waller");

        CustomerWalletService customerWalletService=new CustomerWalletService(customer, 50);

        customerWalletService.addMoney(150.0f);
        System.out.println("Customer: " + customerWalletService.getCustomerName());
        System.out.println("Balance after deposit: " + customerWalletService.checkBalance());

        boolean success = customerWalletService.makePayment(250.0f);
        System.out.println("Payment successful? " + success);
        System.out.println("Final Balance: " + customerWalletService.checkBalance());
    }
}
