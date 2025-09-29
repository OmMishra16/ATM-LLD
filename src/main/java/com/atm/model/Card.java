package com.atm.model;

import java.time.LocalDate;

public class Card {
    private String cardNumber;
    private String pin;
    private String bankCode;
    private LocalDate expiryDate;
    private boolean isBlocked;
    
    public Card(String cardNumber, String pin, String bankCode, LocalDate expiryDate) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.bankCode = bankCode;
        this.expiryDate = expiryDate;
        this.isBlocked = false;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getPin() {
        return pin;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public String getBankCode() {
        return bankCode;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public boolean isBlocked() {
        return isBlocked;
    }
    
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }
}