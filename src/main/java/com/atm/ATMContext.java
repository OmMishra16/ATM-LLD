package com.atm;

import com.atm.state.ATMState;
import com.atm.state.IdleState;
import com.atm.model.Card;
import com.atm.hardware.CardReader;
import com.atm.hardware.CashDispenser;
import com.atm.hardware.Printer;
import com.atm.service.BankService;
import java.util.concurrent.atomic.AtomicLong;

public class ATMContext {
    private ATMState currentState;
    private Card currentCard;
    private String displayMessage;
    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private Printer printer;
    private BankService bankService;
    private AtomicLong transactionCounter;
    
    public ATMContext(CardReader cardReader, CashDispenser cashDispenser, 
                     Printer printer, BankService bankService) {
        this.currentState = new IdleState(this);
        this.cardReader = cardReader;
        this.cashDispenser = cashDispenser;
        this.printer = printer;
        this.bankService = bankService;
        this.transactionCounter = new AtomicLong(1);
        this.displayMessage = "Welcome! Please insert your card";
    }
    
    public void setState(ATMState state) {
        this.currentState = state;
    }
    
    public ATMState getState() {
        return currentState;
    }
    
    public Card getCurrentCard() {
        return currentCard;
    }
    
    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }
    
    public String getDisplayMessage() {
        return displayMessage;
    }
    
    public void setDisplayMessage(String message) {
        this.displayMessage = message;
    }
    
    public CardReader getCardReader() {
        return cardReader;
    }
    
    public CashDispenser getCashDispenser() {
        return cashDispenser;
    }
    
    public Printer getPrinter() {
        return printer;
    }
    
    public BankService getBankService() {
        return bankService;
    }
    
    public String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + "_" + transactionCounter.getAndIncrement();
    }
    
    public void ejectCard() {
        if (currentCard != null) {
            cardReader.ejectCard();
            currentCard = null;
        }
    }
    
    public void insertCard(Card card) {
        currentState.insertCard(card);
    }
    
    public void enterPIN(String pin) {
        currentState.enterPIN(pin);
    }
    
    public void selectTransaction() {
        currentState.selectTransaction();
    }
    
    public void withdraw(java.math.BigDecimal amount) {
        currentState.withdraw(amount);
    }
    
    public void deposit(java.math.BigDecimal amount) {
        currentState.deposit(amount);
    }
    
    public void checkBalance() {
        currentState.checkBalance();
    }
    
    public void printMiniStatement() {
        currentState.printMiniStatement();
    }
    
    public void changePIN(String newPIN) {
        currentState.changePIN(newPIN);
    }
    
    public void cancelTransaction() {
        currentState.cancelTransaction();
    }
}