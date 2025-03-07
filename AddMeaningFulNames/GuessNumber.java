package AddMeaningFulNames;
import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
  
     public static final int MIN_INPUT_VALUE=1;
     public static final int MAX_INPUT_VALUE=100;
        public static void main(String[] args) {
            try(Scanner scanner=new Scanner(System.in)){

                int randomNumber=new Random().nextInt(MIN_INPUT_VALUE,MAX_INPUT_VALUE+1);
                boolean isGuessNumber=false;
                System.out.print("Guess a number between 1 and 100:");
                String userGuessNumber=scanner.next();
                
                int guessAttempt=0;
                while(!isGuessNumber){
                    if(!isDigit(userGuessNumber)){
                     System.out.print("I wont count this one Please enter a number between 1 to 100:");
                     userGuessNumber=scanner.next();
                     continue;
                    }else{
                        guessAttempt=guessAttempt+1;
                    }

                    if(Integer.parseInt(userGuessNumber)<randomNumber){
                        System.out.print("Too low. Guess again: ");
                        userGuessNumber=scanner.next();
                    }else if(Integer.parseInt(userGuessNumber)>randomNumber){
                        System.out.print("Too High. Guess again: ");
                        userGuessNumber=scanner.next();
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
        if( Integer.parseInt(input)>=MIN_INPUT_VALUE &&Integer.parseInt(input)<=MAX_INPUT_VALUE){
           return true; 
        }
        return false;
    }
}
