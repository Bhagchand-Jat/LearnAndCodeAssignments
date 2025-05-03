package atm_machine.validation;

import atm_machine.exception.ATMException;

public class BasicPinValidator implements PinValidator {
    private final PinMatcher pinMatcher;
    private final PinAttemptManager pinAttemptManager;

    public BasicPinValidator(String pin, int limit) {
        this.pinMatcher = new PinMatcher(pin);
        this.pinAttemptManager = new PinAttemptManager(limit);
    }

    @Override
    public boolean validate(String inputPin) throws ATMException {
        if (pinAttemptManager.isBlocked()) {
            throw new ATMException("Card is blocked due to multiple invalid attempts.");
        }

        if (pinMatcher.matches(inputPin)) {
            pinAttemptManager.reset();
            return true;
        } else {
            pinAttemptManager.recordFailure();
            if (pinAttemptManager.isBlocked()) {
                throw new ATMException("Card blocked after " + pinAttemptManager.getMaxAttempts() + " invalid PIN attempts.");
            } else {
                throw new ATMException("Invalid PIN. Attempt " + pinAttemptManager.getAttempts() + "/" + pinAttemptManager.getMaxAttempts());
            }
        }
    }
}

