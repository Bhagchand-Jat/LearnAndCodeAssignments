package customer.service;
public interface CustomerService {
    boolean makePayment(float amount);
    void addMoney(float amount);
    float checkBalance();
    String getCustomerName();
}
