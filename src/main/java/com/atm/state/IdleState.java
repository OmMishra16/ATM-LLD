package com.atm.state;

import com.atm.ATMContext;
import com.atm.model.Card;
import java.math.BigDecimal;

public class IdleState implements ATMState {
    private ATMContext context;
    
    public IdleState(ATMContext context) {
        this.context = context;
    }
    
    @Override
    public void insertCard(Card card) {
        if (card == null) {
            context.setDisplayMessage("Invalid card. Please try again.");
            return;
        }
        
        if (card.isExpired()) {
            context.setDisplayMessage("Card expired. Please contact your bank.");
            context.ejectCard();
            return;
        }
        
        if (card.isBlocked()) {
            context.setDisplayMessage("Card blocked. Please contact your bank.");
            context.ejectCard();
            return;
        }
        
        context.setCurrentCard(card);
        context.setState(new CardInsertedState(context));
        context.setDisplayMessage("Please enter your PIN");
    }
    
    @Override
    public void ejectCard() {
        context.setDisplayMessage("No card inserted");
    }
    
    @Override
    public void enterPIN(String pin) {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void selectTransaction() {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void withdraw(BigDecimal amount) {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void deposit(BigDecimal amount) {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void checkBalance() {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void printMiniStatement() {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void changePIN(String newPIN) {
        context.setDisplayMessage("Please insert your card first");
    }
    
    @Override
    public void cancelTransaction() {
        context.setDisplayMessage("No transaction to cancel");
    }
    
    @Override
    public String getDisplayMessage() {
        return "Welcome! Please insert your card";
    }
}