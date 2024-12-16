package AddMeaningFulNames;
import java.util.Scanner;

public class ArmstrongNumber {

    public static int findArmstrongNumber(int number) {
        int sum = 0;
        int digitsInNumber = 0;
        int individualDigits = number;

        while (individualDigits > 0) {
            digitsInNumber = digitsInNumber + 1;
            individualDigits = individualDigits / 10;
        }

        individualDigits = number;
        for (int index = 1; index <= digitsInNumber; index++) {
            int remainder = individualDigits % 10;
            sum = sum + (int) Math.pow(remainder, digitsInNumber);
            individualDigits /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please Enter the Number to Check for Armstrong: ");
            int number = scanner.nextInt();
            if (number == findArmstrongNumber(number)) {
                System.out.println(number + " is Armstrong Number.");
            } else {
                System.out.println(number + " is Not a Armstrong Number.");
            }
        }
    }

}
