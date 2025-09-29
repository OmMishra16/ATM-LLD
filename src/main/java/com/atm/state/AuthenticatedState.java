package com.atm.state;

import com.atm.ATMContext;
import com.atm.model.Card;
import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.exception.InsufficientCashException;
import java.math.BigDecimal;
import java.util.List;

public class AuthenticatedState implements ATMState {
    private ATMContext context;
    
    public AuthenticatedState(ATMContext context) {
        this.context = context;
    }
    
    @Override
    public void insertCard(Card card) {
        context.setDisplayMessage("Card already inserted and authenticated");
    }
    
    @Override
    public void ejectCard() {
        context.setCurrentCard(null);
        context.setState(new IdleState(context));
        context.setDisplayMessage("Thank you for using our ATM. Card ejected.");
    }
    
    @Override
    public void enterPIN(String pin) {
        context.setDisplayMessage("Already authenticated. Please select a transaction");
    }
    
    @Override
    public void selectTransaction() {
        context.setDisplayMessage("Select: 1-Withdraw 2-Deposit 3-Balance 4-Mini Statement 5-Change PIN");
    }
    
    @Override
    public void withdraw(BigDecimal amount) {
        Account account = context.getBankService().getAccount(context.getCurrentCard().getCardNumber());
        
        if (account == null) {
            context.setDisplayMessage("Account not found");
            return;
        }
        
        if (!context.getCashDispenser().canDispense(amount)) {
            context.setDisplayMessage("Unable to dispense requested amount");
            return;
        }
        
        if (account.debit(amount)) {
            try {
                context.getCashDispenser().dispenseCash(amount);
                
                Transaction transaction = new Transaction(
                    context.generateTransactionId(),
                    Transaction.TransactionType.WITHDRAWAL,
                    amount,
                    "Cash withdrawal",
                    account.getBalance()
                );
                account.addTransaction(transaction);
                
                context.getPrinter().printReceipt(transaction, account);
                context.setDisplayMessage("Transaction completed. Please take your cash and receipt");
            } catch (InsufficientCashException e) {
                account.credit(amount);
                context.setDisplayMessage("Unable to dispense cash: " + e.getMessage());
            }
        } else {
            context.setDisplayMessage("Insufficient funds");
        }
    }
    
    @Override
    public void deposit(BigDecimal amount) {
        Account account = context.getBankService().getAccount(context.getCurrentCard().getCardNumber());
        
        if (account == null) {
            context.setDisplayMessage("Account not found");
            return;
        }
        
        account.credit(amount);
        
        Transaction transaction = new Transaction(
            context.generateTransactionId(),
            Transaction.TransactionType.DEPOSIT,
            amount,
            "Cash deposit",
            account.getBalance()
        );
        account.addTransaction(transaction);
        
        context.getPrinter().printReceipt(transaction, account);
        context.setDisplayMessage("Deposit successful. Please take your receipt");
    }
    
    @Override
    public void checkBalance() {
        Account account = context.getBankService().getAccount(context.getCurrentCard().getCardNumber());
        
        if (account == null) {
            context.setDisplayMessage("Account not found");
            return;
        }
        
        Transaction transaction = new Transaction(
            context.generateTransactionId(),
            Transaction.TransactionType.BALANCE_INQUIRY,
            BigDecimal.ZERO,
            "Balance inquiry",
            account.getBalance()
        );
        account.addTransaction(transaction);
        
        context.setDisplayMessage("Your balance: $" + account.getBalance());
        context.getPrinter().printReceipt(transaction, account);
    }
    
    @Override
    public void printMiniStatement() {
        Account account = context.getBankService().getAccount(context.getCurrentCard().getCardNumber());
        
        if (account == null) {
            context.setDisplayMessage("Account not found");
            return;
        }
        
        List<Transaction> recentTransactions = account.getTransactionHistory();
        context.getPrinter().printMiniStatement(recentTransactions, account);
        context.setDisplayMessage("Mini statement printed");
    }
    
    @Override
    public void changePIN(String newPIN) {
        Card card = context.getCurrentCard();
        card.setPin(newPIN);
        
        Account account = context.getBankService().getAccount(card.getCardNumber());
        Transaction transaction = new Transaction(
            context.generateTransactionId(),
            Transaction.TransactionType.PIN_CHANGE,
            BigDecimal.ZERO,
            "PIN changed",
            account.getBalance()
        );
        account.addTransaction(transaction);
        
        context.setDisplayMessage("PIN changed successfully");
    }
    
    @Override
    public void cancelTransaction() {
        ejectCard();
    }
    
    @Override
    public String getDisplayMessage() {
        return "Please select a transaction";
    }
}