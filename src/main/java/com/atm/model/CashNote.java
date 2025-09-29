package com.atm.model;

public class CashNote {
    private int denomination;
    private int count;
    
    public CashNote(int denomination, int count) {
        this.denomination = denomination;
        this.count = count;
    }
    
    public int getDenomination() {
        return denomination;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public int getTotalValue() {
        return denomination * count;
    }
    
    public boolean dispense(int quantity) {
        if (count >= quantity) {
            count -= quantity;
            return true;
        }
        return false;
    }
    
    public void deposit(int quantity) {
        count += quantity;
    }
}