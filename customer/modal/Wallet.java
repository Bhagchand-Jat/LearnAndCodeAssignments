package customer.modal;
public class Wallet {
    private float balance;

    public Wallet(float initialBalance) {
        this.balance = initialBalance;
    }

    public float getTotalMoney() {
        return balance;
    }

    public void credit(float deposit) {
        this.balance += deposit;
    }

    public boolean debit(float debit) {
        if (balance >= debit) {
            balance -= debit;
            return true;
        }
        return false;
        
    }
}
