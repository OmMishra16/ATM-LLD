package com.atm.state;

import com.atm.ATMContext;
import com.atm.model.Card;
import java.math.BigDecimal;

public class CardInsertedState implements ATMState {
    private ATMContext context;
    private int pinAttempts = 0;
    private static final int MAX_PIN_ATTEMPTS = 3;
    
    public CardInsertedState(ATMContext context) {
        this.context = context;
    }
    
    @Override
    public void insertCard(Card card) {
        context.setDisplayMessage("Card already inserted");
    }
    
    @Override
    public void ejectCard() {
        context.setCurrentCard(null);
        context.setState(new IdleState(context));
        context.setDisplayMessage("Card ejected");
    }
    
    @Override
    public void enterPIN(String pin) {
        Card currentCard = context.getCurrentCard();
        
        if (currentCard.getPin().equals(pin)) {
            context.setState(new AuthenticatedState(context));
            context.setDisplayMessage("PIN accepted. Please select a transaction");
        } else {
            pinAttempts++;
            if (pinAttempts >= MAX_PIN_ATTEMPTS) {
                currentCard.setBlocked(true);
                context.setDisplayMessage("Too many incorrect attempts. Card blocked.");
                ejectCard();
            } else {
                context.setDisplayMessage("Incorrect PIN. " + (MAX_PIN_ATTEMPTS - pinAttempts) + " attempts remaining");
            }
        }
    }
    
    @Override
    public void selectTransaction() {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void withdraw(BigDecimal amount) {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void deposit(BigDecimal amount) {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void checkBalance() {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void printMiniStatement() {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void changePIN(String newPIN) {
        context.setDisplayMessage("Please enter your PIN first");
    }
    
    @Override
    public void cancelTransaction() {
        ejectCard();
    }
    
    @Override
    public String getDisplayMessage() {
        return "Please enter your PIN";
    }
}