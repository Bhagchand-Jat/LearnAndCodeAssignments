package AddMeaningFulNames;
import java.util.Random;
import java.util.Scanner;

public class Dice {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int maxDiceValue = 6;
            boolean isDiceRolled = false;
            while (!isDiceRolled) {
                System.out.println("Ready to roll? Enter Q to Quit");
                String readyToRoll = scanner.nextLine();
                if (!readyToRoll.toLowerCase().equals("q")) {
                    int diceRollResult = rollDice(maxDiceValue);
                    System.out.println("You have rolled a " + diceRollResult);
                } else {
                    isDiceRolled = true;
                }
            }
        }
    }

    public static int rollDice(int maxDiceValue) {
        return new Random().nextInt(1, maxDiceValue + 1);
    }

}
