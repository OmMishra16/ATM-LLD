package com.atm;

import com.atm.model.Card;
import com.atm.service.BankServiceFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class ATMDemo {
    private static ATMMachine atm;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        atm = new ATMMachine(BankServiceFactory.createDefaultBankService());
        scanner = new Scanner(System.in);
        
        System.out.println("=== ATM System Demo ===");
        System.out.println("Available test cards:");
        System.out.println("Chase Bank - Card: 1234567890123456, PIN: 1234");
        System.out.println("Chase Bank - Card: 9876543210987654, PIN: 5678");
        System.out.println();
        
        while (true) {
            System.out.println(atm.getDisplayMessage());
            System.out.println("\nOptions:");
            
            if (!atm.isCardInserted()) {
                System.out.println("1. Insert Card");
                System.out.println("0. Exit");
            } else {
                System.out.println("1. Enter PIN");
                System.out.println("2. Withdraw Money");
                System.out.println("3. Deposit Money");
                System.out.println("4. Check Balance");
                System.out.println("5. Print Mini Statement");
                System.out.println("6. Change PIN");
                System.out.println("7. Cancel/Eject Card");
                System.out.println("0. Exit");
            }
            
            System.out.print("Choose option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 0:
                        System.out.println("Thank you for using our ATM!");
                        return;
                    case 1:
                        if (!atm.isCardInserted()) {
                            insertCard();
                        } else {
                            enterPIN();
                        }
                        break;
                    case 2:
                        withdrawMoney();
                        break;
                    case 3:
                        depositMoney();
                        break;
                    case 4:
                        atm.checkBalance();
                        break;
                    case 5:
                        atm.printMiniStatement();
                        break;
                    case 6:
                        changePIN();
                        break;
                    case 7:
                        atm.cancelTransaction();
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please try again.");
                scanner.nextLine();
            }
            
            System.out.println("\n" + "=".repeat(50) + "\n");
        }
    }
    
    private static void insertCard() {
        System.out.print("Enter card number (16 digits): ");
        String cardNumber = scanner.nextLine();
        
        String pin;
        if ("1234567890123456".equals(cardNumber)) {
            pin = "1234";
        } else if ("9876543210987654".equals(cardNumber)) {
            pin = "5678";
        } else {
            pin = "0000";
        }
        
        Card card = new Card(cardNumber, pin, "CHASE", LocalDate.of(2027, 12, 31));
        atm.insertCard(card);
    }
    
    private static void enterPIN() {
        System.out.print("Enter PIN (4 digits): ");
        String pin = scanner.nextLine();
        atm.enterPIN(pin);
    }
    
    private static void withdrawMoney() {
        System.out.print("Enter amount to withdraw: $");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            atm.withdraw(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format!");
        }
    }
    
    private static void depositMoney() {
        System.out.print("Enter amount to deposit: $");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            atm.deposit(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format!");
        }
    }
    
    private static void changePIN() {
        System.out.print("Enter new PIN (4 digits): ");
        String newPIN = scanner.nextLine();
        
        if (newPIN.length() == 4 && newPIN.matches("\\d{4}")) {
            atm.changePIN(newPIN);
        } else {
            System.out.println("PIN must be 4 digits!");
        }
    }
}