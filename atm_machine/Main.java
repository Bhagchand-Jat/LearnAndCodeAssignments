package atm_machine;

import java.util.Scanner;

import atm_machine.exception.ATMException;
import atm_machine.model.Account;
import atm_machine.model.BasicAccount;
import atm_machine.service.ATMService;

public class Main{
     public static void main(String[] args) {
        Account account = new BasicAccount("12345678", "1234",3000, 100);
        ATMService atmService = new ATMService(50000);
        Scanner scanner = new Scanner(System.in);

        try {
           boolean isPinValid=false;
            do{
                System.out.print("Enter PIN: ");
                String pin = scanner.nextLine();
                 isPinValid=atmService.validatePin(account,pin);
                 if(!isPinValid){
                    System.out.println("Invalid Pin : Eneter correct pin Again.");
                 }
            }while(!isPinValid);
            if (isPinValid) {
                char choice;
                do {
                    System.out.print("Enter amount to withdraw: ");
                    double amount = scanner.nextDouble();
            
                    atmService.withdrawFromATM(account, amount);
                    System.out.println("Withdrawal successful!");
                    System.out.println("Remaining account balance: " + account.getBalance());
            
                    System.out.print("Do you want to withdraw again? (y/n or other key): ");
                    choice = scanner.next().charAt(0);
                    scanner.nextLine(); 
                } while (choice == 'y' || choice == 'Y');
            }else{

            }
            
        } catch (ATMException e) {
            System.err.println("ATM Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}