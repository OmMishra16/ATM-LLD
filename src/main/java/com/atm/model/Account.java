package com.atm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private String bankCode;
    private List<Transaction> transactionHistory;
    
    public Account(String accountNumber, BigDecimal balance, String bankCode) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bankCode = bankCode;
        this.transactionHistory = new ArrayList<>();
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public String getBankCode() {
        return bankCode;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public boolean debit(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
    
    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}