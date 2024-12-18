package AddMeaningFulNames;
import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
  
     public static final int inputMinValue=1;
     public static final int inputMaxValue=100;
        public static void main(String[] args) {
            try(Scanner scanner=new Scanner(System.in)){

                int randomNumber=new Random().nextInt(inputMinValue,inputMaxValue+1);
                boolean isGuessNumber=false;
                System.out.print("Guess a number between 1 and 100:");
                String guessNumber=scanner.next();
                
                int guessAttempt=0;
                while(!isGuessNumber){
                    if(!isDigit(guessNumber)){
                     System.out.print("I wont count this one Please enter a number between 1 to 100:");
                     guessNumber=scanner.next();
                     continue;
                    }else{
                        guessAttempt=guessAttempt+1;
                    }

                    if(Integer.parseInt(guessNumber)<randomNumber){
                        System.out.print("Too low. Guess again: ");
                        guessNumber=scanner.next();
                    }else if(Integer.parseInt(guessNumber)>randomNumber){
                        System.out.print("Too High. Guess again: ");
                        guessNumber=scanner.next();
                    }else{
                        System.out.print("You guessed it in "+guessAttempt+" guesses!");
                        isGuessNumber=true;
                    }
                }
            }
         
    }

    public static boolean isDigit(String input){
      
        for (char numberChar : input.toCharArray()) {
            
            if(!Character.isDigit(numberChar)){
                  return false;
            }
        }
        if( Integer.parseInt(input)>=inputMinValue &&Integer.parseInt(input)<=inputMaxValue){
           return true; 
        }
        return false;
    }
}
