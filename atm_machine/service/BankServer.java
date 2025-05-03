package atm_machine.service;

import java.util.Random;

public class BankServer {
    private static Random random = new Random();

    public static boolean isServerAvailable() {
        return random.nextInt(100) >= 20;
    }
}
