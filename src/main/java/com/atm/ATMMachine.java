package com.atm;

import com.atm.hardware.*;
import com.atm.service.BankService;
import com.atm.service.BankServiceFactory;
import com.atm.model.Card;
import java.math.BigDecimal;

public class ATMMachine {
    private ATMContext context;
    private CardReader cardReader;
    private CashDispenser cashDispenser;
    private Printer printer;
    private PINPad pinPad;
    private DepositSlot depositSlot;
    private BankService bankService;
    
    public ATMMachine() {
        this.cardReader = new CardReader();
        this.cashDispenser = new CashDispenser();
        this.printer = new Printer();
        this.pinPad = new PINPad();
        this.depositSlot = new DepositSlot();
        this.bankService = BankServiceFactory.createDefaultBankService();
        
        this.context = new ATMContext(cardReader, cashDispenser, printer, bankService);
    }
    
    public ATMMachine(BankService bankService) {
        this.cardReader = new CardReader();
        this.cashDispenser = new CashDispenser();
        this.printer = new Printer();
        this.pinPad = new PINPad();
        this.depositSlot = new DepositSlot();
        this.bankService = bankService;
        
        this.context = new ATMContext(cardReader, cashDispenser, printer, bankService);
    }
    
    public void insertCard(Card card) {
        if (cardReader.insertCard(card)) {
            context.insertCard(card);
        } else {
            context.setDisplayMessage("Unable to read card. Please try again.");
        }
    }
    
    public void enterPIN(String pin) {
        context.enterPIN(pin);
    }
    
    public void withdraw(BigDecimal amount) {
        context.withdraw(amount);
    }
    
    public void deposit(BigDecimal amount) {
        depositSlot.openSlot();
        context.deposit(amount);
        depositSlot.closeSlot();
    }
    
    public void checkBalance() {
        context.checkBalance();
    }
    
    public void printMiniStatement() {
        context.printMiniStatement();
    }
    
    public void changePIN(String newPIN) {
        context.changePIN(newPIN);
    }
    
    public void cancelTransaction() {
        context.cancelTransaction();
    }
    
    public void ejectCard() {
        context.ejectCard();
    }
    
    public String getDisplayMessage() {
        return context.getDisplayMessage();
    }
    
    public boolean isCardInserted() {
        return cardReader.isCardInserted();
    }
    
    public Card getCurrentCard() {
        return context.getCurrentCard();
    }
    
    public ATMContext getContext() {
        return context;
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
    
    public PINPad getPinPad() {
        return pinPad;
    }
    
    public DepositSlot getDepositSlot() {
        return depositSlot;
    }
    
    public BankService getBankService() {
        return bankService;
    }
}