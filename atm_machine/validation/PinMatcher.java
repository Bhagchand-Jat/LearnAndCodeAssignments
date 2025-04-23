package atm_machine.validation;

public class PinMatcher {
    private final String actualPin;

    public PinMatcher(String actualPin) {
        this.actualPin = actualPin;
    }

    public boolean matches(String inputPin) {
        return actualPin.equals(inputPin);
    }
}

