package atm_machine.validation;

import atm_machine.exception.ATMException;
import atm_machine.model.Account;
import atm_machine.service.BankServer;

public class DefaultWithdrawalValidator implements WithdrawalValidator {


    @Override
    public void withdraw(Account account, double amount,double atmBalance) throws ATMException {
        if (!BankServer.isServerAvailable()) {
            throw new ATMException("Unable to connect to bank server. Please try again later.");
        }

        if(atmBalance<amount){
            throw new ATMException("Insufficient funds in ATM Machine.");
        }
        if (amount > account.getBalance()) {
            throw new ATMException("Insufficient funds in your account.");
        }
        if ((account.getWithdrawnToday() + amount) > account.getDailyLimit()) {
            throw new ATMException("Daily limit exceeded. Limit: " + account.getDailyLimit());
        }

    }
}
