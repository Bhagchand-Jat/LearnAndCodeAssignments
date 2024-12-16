package AddMeaningFulNames;
import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
    public static boolean isDigit(String number){
      
        for (char numberChar : number.toCharArray()) {
            
            if(!Character.isDigit(numberChar)){
                  return false;
            }
        }
        if( Integer.parseInt(number)>=1 &&Integer.parseInt(number)<=100){
           return true; 
        }
        return false;
    }
  
        public static void main(String[] args) {
            try(Scanner scanner=new Scanner(System.in)){

                int randomNumber=new Random().nextInt(1,101);
                boolean isGuessNumber=false;
                System.out.print("Guess a number between 1 and 100:");
                String guessNumber=scanner.next();
                
                int countOfGuess=0;
                while(!isGuessNumber){
                    if(!isDigit(guessNumber)){
                     System.out.print("I wont count this one Please enter a number between 1 to 100:");
                     guessNumber=scanner.next();
                     continue;
                    }else{
                        countOfGuess=countOfGuess+1;
                    }

                    if(Integer.parseInt(guessNumber)<randomNumber){
                        System.out.print("Too low. Guess again: ");
                        guessNumber=scanner.next();
                    }else if(Integer.parseInt(guessNumber)>randomNumber){
                        System.out.print("Too High. Guess again: ");
                        guessNumber=scanner.next();
                    }else{
                        System.out.print("You guessed it in "+countOfGuess+" guesses!");
                        isGuessNumber=true;
                    }
                }
            }
         
    }
}
