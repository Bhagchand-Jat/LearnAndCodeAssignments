package AddMeaningFulNames;
import java.util.Scanner;

public class ArmstrongNumber {

    
    private static final int BASE_DIVISOR = 10;   
    private static final int INITIAL_COUNT = 0; 
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please Enter the Number to Check for Armstrong: ");
            int inputNumber = scanner.nextInt();
            int powerSumofDigits=calculatePowerSumOfDigits(inputNumber);
            if (isArmStrongNumber(inputNumber,powerSumofDigits)) {
                System.out.println(inputNumber + " is Armstrong Number.");
            } else {
                System.out.println(inputNumber + " is Not a Armstrong Number.");
            }
        }
    }

    public static int calculatePowerSumOfDigits(int inputNumber) {
        int sumOfDigitsToDigitCountPower = INITIAL_COUNT;
        int digitCount = INITIAL_COUNT;
        int individualDigits = inputNumber;

        while (individualDigits > 0) {
            digitCount = digitCount + 1;
            individualDigits = individualDigits / BASE_DIVISOR;
        }

        individualDigits = inputNumber;
        for (int index = 1; index <= digitCount; index++) {
            int remainder = individualDigits % BASE_DIVISOR;
            sumOfDigitsToDigitCountPower = sumOfDigitsToDigitCountPower + (int) Math.pow(remainder, digitCount);
            individualDigits /= BASE_DIVISOR;
        }
        return sumOfDigitsToDigitCountPower;
    }

  public static boolean isArmStrongNumber(int inputNumber,int powerSumofDigits){
    if(inputNumber==powerSumofDigits){
        return true;
    }
    return false;
  }

}
