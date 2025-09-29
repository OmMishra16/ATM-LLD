package com.atm.hardware;

import com.atm.model.CashNote;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DepositSlot {
    private Map<Integer, Integer> depositedNotes;
    private boolean isOpen;
    private boolean hasDeposit;
    
    public DepositSlot() {
        this.depositedNotes = new HashMap<>();
        this.isOpen = false;
        this.hasDeposit = false;
    }
    
    public void openSlot() {
        this.isOpen = true;
        this.depositedNotes.clear();
        this.hasDeposit = false;
    }
    
    public void closeSlot() {
        this.isOpen = false;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    public void insertNotes(int denomination, int count) {
        if (!isOpen) {
            throw new IllegalStateException("Deposit slot is closed");
        }
        
        depositedNotes.put(denomination, depositedNotes.getOrDefault(denomination, 0) + count);
        hasDeposit = true;
    }
    
    public BigDecimal calculateDepositAmount() {
        if (!hasDeposit) {
            return BigDecimal.ZERO;
        }
        
        int totalAmount = depositedNotes.entrySet().stream()
            .mapToInt(entry -> entry.getKey() * entry.getValue())
            .sum();
        
        return BigDecimal.valueOf(totalAmount);
    }
    
    public Map<Integer, Integer> getDepositedNotes() {
        return new HashMap<>(depositedNotes);
    }
    
    public boolean hasDeposit() {
        return hasDeposit;
    }
    
    public void clearDeposit() {
        depositedNotes.clear();
        hasDeposit = false;
    }
    
    public boolean validateDeposit() {
        if (!hasDeposit) {
            return false;
        }
        
        for (Map.Entry<Integer, Integer> entry : depositedNotes.entrySet()) {
            if (entry.getValue() <= 0) {
                return false;
            }
        }
        
        return true;
    }
}