package com.atm.hardware;

import com.atm.model.Card;

public class CardReader {
    private Card insertedCard;
    private boolean isCardInserted;
    
    public CardReader() {
        this.isCardInserted = false;
    }
    
    public boolean insertCard(Card card) {
        if (isCardInserted) {
            return false;
        }
        
        if (card == null) {
            return false;
        }
        
        this.insertedCard = card;
        this.isCardInserted = true;
        return true;
    }
    
    public Card ejectCard() {
        if (!isCardInserted) {
            return null;
        }
        
        Card card = this.insertedCard;
        this.insertedCard = null;
        this.isCardInserted = false;
        return card;
    }
    
    public boolean isCardInserted() {
        return isCardInserted;
    }
    
    public Card getInsertedCard() {
        return insertedCard;
    }
    
    public boolean readCard() {
        if (!isCardInserted || insertedCard == null) {
            return false;
        }
        
        return !insertedCard.isExpired() && !insertedCard.isBlocked();
    }
}