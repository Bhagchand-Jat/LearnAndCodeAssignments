package AddMeaningFulNames;
import java.util.Random;
import java.util.Scanner;

public class Dice {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int Dice_Max_Value = 6;
            boolean isDiceNotRolled = true;
            while (isDiceNotRolled) {
                System.out.println("Ready to roll? Enter Q to Quit");
                String readyToRoll = scanner.nextLine();
                if (!readyToRoll.toLowerCase().equals("q")) {
                    int diceValue = rollDice(Dice_Max_Value);
                    System.out.println("You have rolled a " + diceValue);
                } else {
                    isDiceNotRolled = false;
                }
            }
        }
    }

    public static int rollDice(int Dice_Max_Value) {
        return new Random().nextInt(1, Dice_Max_Value + 1);
    }

}
