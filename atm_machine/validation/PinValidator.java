package atm_machine.validation;

import atm_machine.exception.ATMException;

public interface PinValidator {
    boolean validate(String inputPin) throws ATMException;
}
