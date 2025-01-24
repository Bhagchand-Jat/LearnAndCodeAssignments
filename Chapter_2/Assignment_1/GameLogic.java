package Chapter_2.Assignment_1;

import java.util.Scanner;
import java.util.Random;

class GameLogic {

    public static boolean isValidGuess(String input) {
        try {
            int number = Integer.parseInt(input);
            return number >= 1 && number <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getValidGuess(Scanner scanner) {
        String input;
        while (true) {
            input = scanner.nextLine();
            if (isValidGuess(input)) {
                return Integer.parseInt(input);
            }
            System.out.println("Please enter a valid number between 1 and 100:");
        }
    }

    public static void playGame() {
        Random random = new Random();
        int targetNumber = random.nextInt(100) + 1;
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        int guess;

        System.out.println("Guess a number between 1 and 100:");

        while (true) {
            guess = getValidGuess(scanner);
            attempts++;

            if (guess < targetNumber) {
                System.out.println("Too low. Guess again:");
            } else if (guess > targetNumber) {
                System.out.println("Too high. Guess again:");
            } else {
                System.out.println("You guessed it in " + attempts + " guesses!");
                break;
            }
        }

        scanner.close();
    }
}
