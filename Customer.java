public class Customer {
    private String firstName;
    private String lastName;
    private Wallet wallet;

    public Customer(String firstName, String lastName, float initialBalance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.wallet = new Wallet(initialBalance);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean makePayment(float amount) {
        if (wallet.getTotalMoney()>=amount) {
            wallet.subtractMoney(amount);
            return true;
        }
        return false;
    }

    public void addMoney(float amount) {
        wallet.addMoney(amount);
    }

    public float checkBalance() {
        return wallet.getTotalMoney();
    }
}
