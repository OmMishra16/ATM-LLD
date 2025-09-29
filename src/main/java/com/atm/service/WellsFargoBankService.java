package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Card;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WellsFargoBankService implements BankService {
    private Map<String, Account> accounts;
    private Map<String, Card> cards;
    private static final String BANK_CODE = "WF";
    private static final String BANK_NAME = "Wells Fargo";
    
    public WellsFargoBankService() {
        this.accounts = new HashMap<>();
        this.cards = new HashMap<>();
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        Card card1 = new Card("4444555566667777", "9999", BANK_CODE, LocalDate.of(2028, 3, 31));
        Account account1 = new Account("2001", new BigDecimal("7500.00"), BANK_CODE);
        
        Card card2 = new Card("3333222211110000", "8888", BANK_CODE, LocalDate.of(2025, 9, 30));
        Account account2 = new Account("2002", new BigDecimal("1200.00"), BANK_CODE);
        
        cards.put(card1.getCardNumber(), card1);
        accounts.put(card1.getCardNumber(), account1);
        
        cards.put(card2.getCardNumber(), card2);
        accounts.put(card2.getCardNumber(), account2);
    }
    
    @Override
    public Account getAccount(String cardNumber) {
        return accounts.get(cardNumber);
    }
    
    @Override
    public boolean validateCard(Card card) {
        Card storedCard = cards.get(card.getCardNumber());
        return storedCard != null && 
               storedCard.getBankCode().equals(card.getBankCode()) &&
               !storedCard.isExpired() && 
               !storedCard.isBlocked();
    }
    
    @Override
    public boolean validatePIN(String cardNumber, String pin) {
        Card card = cards.get(cardNumber);
        return card != null && card.getPin().equals(pin);
    }
    
    @Override
    public boolean updatePIN(String cardNumber, String newPIN) {
        Card card = cards.get(cardNumber);
        if (card != null) {
            card.setPin(newPIN);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean processWithdrawal(String cardNumber, BigDecimal amount) {
        Account account = accounts.get(cardNumber);
        if (account != null && account.getBalance().compareTo(amount) >= 0) {
            account.debit(amount);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean processDeposit(String cardNumber, BigDecimal amount) {
        Account account = accounts.get(cardNumber);
        if (account != null) {
            account.credit(amount);
            return true;
        }
        return false;
    }
    
    @Override
    public BigDecimal getAccountBalance(String cardNumber) {
        Account account = accounts.get(cardNumber);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }
    
    @Override
    public boolean isCardBlocked(String cardNumber) {
        Card card = cards.get(cardNumber);
        return card != null && card.isBlocked();
    }
    
    @Override
    public void blockCard(String cardNumber) {
        Card card = cards.get(cardNumber);
        if (card != null) {
            card.setBlocked(true);
        }
    }
    
    @Override
    public String getBankCode() {
        return BANK_CODE;
    }
    
    @Override
    public String getBankName() {
        return BANK_NAME;
    }
}