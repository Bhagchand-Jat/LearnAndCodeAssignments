package atm_machine.service;

import atm_machine.exception.ATMException;
import atm_machine.model.Account;
import atm_machine.validation.DefaultWithdrawalValidator;
import atm_machine.validation.PinMatcher;

public class ATMService {
    private double atmBalance;
    
    DefaultWithdrawalValidator withdrawalValidator=new DefaultWithdrawalValidator();

    public ATMService(double atmBalance) {
        this.atmBalance = atmBalance;
    }

    public boolean validatePin(Account account,String inputPin){
        PinMatcher pinMatcher=new PinMatcher(account.getAtmPin());
        return pinMatcher.matches(inputPin);
    }

    public void withdrawFromATM(Account account, double amount) throws ATMException {
        DefaultWithdrawalValidator withdrawalValidator=new DefaultWithdrawalValidator();
        withdrawalValidator.withdraw(account, amount, atmBalance);
       account.withdrawMoney(amount);


        
    }

}
