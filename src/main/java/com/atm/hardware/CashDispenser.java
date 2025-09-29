package com.atm.hardware;

import com.atm.model.CashNote;
import com.atm.exception.InsufficientCashException;
import java.math.BigDecimal;
import java.util.*;

public class CashDispenser {
    private Map<Integer, CashNote> cashNotes;
    private List<Integer> availableDenominations;
    
    public CashDispenser() {
        this.cashNotes = new HashMap<>();
        this.availableDenominations = Arrays.asList(100, 50, 20, 10, 5, 1);
        
        for (int denomination : availableDenominations) {
            cashNotes.put(denomination, new CashNote(denomination, 100));
        }
    }
    
    public boolean canDispense(BigDecimal amount) {
        int amountInt = amount.intValue();
        return calculateMinimumNotes(amountInt) != null;
    }
    
    public Map<Integer, Integer> dispenseCash(BigDecimal amount) throws InsufficientCashException {
        int amountInt = amount.intValue();
        Map<Integer, Integer> notesToDispense = calculateMinimumNotes(amountInt);
        
        if (notesToDispense == null) {
            throw new InsufficientCashException("Cannot dispense the requested amount");
        }
        
        for (Map.Entry<Integer, Integer> entry : notesToDispense.entrySet()) {
            int denomination = entry.getKey();
            int count = entry.getValue();
            
            CashNote note = cashNotes.get(denomination);
            if (!note.dispense(count)) {
                throw new InsufficientCashException("Insufficient notes of denomination " + denomination);
            }
        }
        
        return notesToDispense;
    }
    
    private Map<Integer, Integer> calculateMinimumNotes(int amount) {
        Map<Integer, Integer> result = new HashMap<>();
        int remainingAmount = amount;
        
        Collections.sort(availableDenominations, Collections.reverseOrder());
        
        for (int denomination : availableDenominations) {
            CashNote note = cashNotes.get(denomination);
            int notesNeeded = remainingAmount / denomination;
            int notesAvailable = note.getCount();
            
            if (notesNeeded > 0 && notesAvailable > 0) {
                int notesToUse = Math.min(notesNeeded, notesAvailable);
                result.put(denomination, notesToUse);
                remainingAmount -= notesToUse * denomination;
            }
        }
        
        return remainingAmount == 0 ? result : null;
    }
    
    public void loadCash(int denomination, int count) {
        CashNote note = cashNotes.get(denomination);
        if (note != null) {
            note.deposit(count);
        }
    }
    
    public int getTotalCash() {
        return cashNotes.values().stream()
            .mapToInt(CashNote::getTotalValue)
            .sum();
    }
    
    public Map<Integer, Integer> getCashInventory() {
        Map<Integer, Integer> inventory = new HashMap<>();
        for (Map.Entry<Integer, CashNote> entry : cashNotes.entrySet()) {
            inventory.put(entry.getKey(), entry.getValue().getCount());
        }
        return inventory;
    }
    
    public boolean isEmpty() {
        return cashNotes.values().stream()
            .allMatch(note -> note.getCount() == 0);
    }
}