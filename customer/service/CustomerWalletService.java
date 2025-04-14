package customer.service;

import customer.modal.Customer;
import customer.modal.Wallet;

public class CustomerWalletService implements CustomerService{
    private Customer customer;
    private Wallet wallet;

    public CustomerWalletService(Customer customer, float initialBalance) {
        this.customer = customer;
        this.wallet = new Wallet(initialBalance);
    }

    public String getCustomerName() {
        return customer.getFullName();
    }

    public boolean makePayment(float amount) {
        return wallet.debit(amount);
    }

    public void addMoney(float amount) {
        wallet.credit(amount);
    }

    public float checkBalance() {
        return wallet.getTotalMoney();
    }
}
