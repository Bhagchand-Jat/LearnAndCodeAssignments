public class Wallet {
    private float value;

    public Wallet(float initialBalance) {
        this.value = initialBalance;
    }

    public float getTotalMoney() {
        return value;
    }

    public void addMoney(float deposit) {
        value += deposit;
    }

    public void subtractMoney(float debit) {
        value -= debit;
        
    }
}
