package com.atm.state;

import com.atm.model.Card;
import java.math.BigDecimal;

public interface ATMState {
    void insertCard(Card card);
    void ejectCard();
    void enterPIN(String pin);
    void selectTransaction();
    void withdraw(BigDecimal amount);
    void deposit(BigDecimal amount);
    void checkBalance();
    void printMiniStatement();
    void changePIN(String newPIN);
    void cancelTransaction();
    String getDisplayMessage();
}