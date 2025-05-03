package atm_machine.model;

import atm_machine.exception.ATMException;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected double dailyLimit;
    protected double withdrawnToday;
    protected String atmPin;
    

    public Account(String accountNumber,String atmPin, double balance, double dailyLimit, double withdrawnToday) {
        this.accountNumber = accountNumber;
        this.atmPin=atmPin;
        this.balance = balance;
        this.dailyLimit = dailyLimit;
        this.withdrawnToday = withdrawnToday;
    }


    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public double getWithdrawnToday() {
        return withdrawnToday;
    }

    public String getAtmPin() {
        return atmPin;
    }

    public abstract void withdrawMoney(double amount) throws ATMException;
}
