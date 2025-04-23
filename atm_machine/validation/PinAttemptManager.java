package atm_machine.validation;

public class PinAttemptManager {
    private int attempts = 0;
    private final int maxAttempts;
    private boolean isBlocked = false;

    public PinAttemptManager(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void recordFailure() {
        attempts++;
        if (attempts >= maxAttempts) {
            isBlocked = true;
        }
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void reset() {
        attempts = 0;
        isBlocked = false;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}

