package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Card;
import java.math.BigDecimal;

public interface BankService {
    Account getAccount(String cardNumber);
    boolean validateCard(Card card);
    boolean validatePIN(String cardNumber, String pin);
    boolean updatePIN(String cardNumber, String newPIN);
    boolean processWithdrawal(String cardNumber, BigDecimal amount);
    boolean processDeposit(String cardNumber, BigDecimal amount);
    BigDecimal getAccountBalance(String cardNumber);
    boolean isCardBlocked(String cardNumber);
    void blockCard(String cardNumber);
    String getBankCode();
    String getBankName();
}