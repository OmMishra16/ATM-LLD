package com.atm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
    private BigDecimal balanceAfter;
    
    public Transaction(String transactionId, TransactionType type, BigDecimal amount, 
                      String description, BigDecimal balanceAfter) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }
    
    public enum TransactionType {
        WITHDRAWAL, DEPOSIT, BALANCE_INQUIRY, PIN_CHANGE, MINI_STATEMENT
    }
}