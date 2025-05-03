package atm_machine.model;

import atm_machine.exception.ATMException;

public class BasicAccount extends Account {

    
    public BasicAccount(String accountNumber,String atmPin, double balance, double dailyLimit) {
        super(accountNumber,atmPin, balance, dailyLimit, 0);
    }

    @Override
    public void withdrawMoney(double amount) throws ATMException{
        this.balance-=amount;
        this.withdrawnToday+=amount;
    }
}

