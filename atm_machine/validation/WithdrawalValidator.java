package atm_machine.validation;

import atm_machine.exception.ATMException;
import atm_machine.model.Account;

public interface WithdrawalValidator {
    void withdraw(Account account, double amount,double atmBalance) throws ATMException;
}
