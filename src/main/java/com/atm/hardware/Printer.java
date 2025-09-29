package com.atm.hardware;

import com.atm.model.Transaction;
import com.atm.model.Account;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Printer {
    private boolean isOnline;
    private boolean hasPaper;
    private StringBuilder printQueue;
    
    public Printer() {
        this.isOnline = true;
        this.hasPaper = true;
        this.printQueue = new StringBuilder();
    }
    
    public boolean printReceipt(Transaction transaction, Account account) {
        if (!canPrint()) {
            return false;
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append("=====================================\n");
        receipt.append("           ATM RECEIPT               \n");
        receipt.append("=====================================\n");
        receipt.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
        receipt.append("Date/Time: ").append(transaction.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("Account: ****").append(account.getAccountNumber().substring(Math.max(0, account.getAccountNumber().length() - 4))).append("\n");
        receipt.append("-------------------------------------\n");
        receipt.append("Transaction: ").append(transaction.getType()).append("\n");
        
        if (transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            receipt.append("Amount: $").append(transaction.getAmount()).append("\n");
        }
        
        receipt.append("Balance: $").append(transaction.getBalanceAfter()).append("\n");
        receipt.append("-------------------------------------\n");
        receipt.append("Thank you for using our ATM service!\n");
        receipt.append("=====================================\n\n");
        
        return print(receipt.toString());
    }
    
    public boolean printMiniStatement(List<Transaction> transactions, Account account) {
        if (!canPrint()) {
            return false;
        }
        
        StringBuilder statement = new StringBuilder();
        statement.append("=====================================\n");
        statement.append("         MINI STATEMENT              \n");
        statement.append("=====================================\n");
        statement.append("Account: ****").append(account.getAccountNumber().substring(Math.max(0, account.getAccountNumber().length() - 4))).append("\n");
        statement.append("Current Balance: $").append(account.getBalance()).append("\n");
        statement.append("-------------------------------------\n");
        statement.append("Recent Transactions:\n");
        statement.append("-------------------------------------\n");
        
        int transactionCount = Math.min(10, transactions.size());
        for (int i = transactions.size() - transactionCount; i < transactions.size(); i++) {
            Transaction tx = transactions.get(i);
            statement.append(tx.getTimestamp().format(DateTimeFormatter.ofPattern("MM/dd HH:mm"))).append(" ");
            statement.append(String.format("%-12s", tx.getType().toString()));
            
            if (tx.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                statement.append(" $").append(String.format("%8s", tx.getAmount()));
            } else {
                statement.append("         ");
            }
            
            statement.append("\n");
        }
        
        statement.append("-------------------------------------\n");
        statement.append("Thank you for using our ATM service!\n");
        statement.append("=====================================\n\n");
        
        return print(statement.toString());
    }
    
    private boolean print(String content) {
        if (!canPrint()) {
            return false;
        }
        
        printQueue.append(content);
        System.out.print(content);
        return true;
    }
    
    public boolean canPrint() {
        return isOnline && hasPaper;
    }
    
    public void setOnline(boolean online) {
        this.isOnline = online;
    }
    
    public void setPaperStatus(boolean hasPaper) {
        this.hasPaper = hasPaper;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    public boolean hasPaper() {
        return hasPaper;
    }
    
    public String getPrintedContent() {
        return printQueue.toString();
    }
    
    public void clearPrintQueue() {
        printQueue.setLength(0);
    }
}