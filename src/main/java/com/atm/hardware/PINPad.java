package com.atm.hardware;

public class PINPad {
    private StringBuilder currentInput;
    private boolean isInputComplete;
    private static final int PIN_LENGTH = 4;
    
    public PINPad() {
        this.currentInput = new StringBuilder();
        this.isInputComplete = false;
    }
    
    public void pressKey(char key) {
        if (Character.isDigit(key) && currentInput.length() < PIN_LENGTH) {
            currentInput.append(key);
            
            if (currentInput.length() == PIN_LENGTH) {
                isInputComplete = true;
            }
        }
    }
    
    public void pressEnter() {
        if (currentInput.length() == PIN_LENGTH) {
            isInputComplete = true;
        }
    }
    
    public void pressClear() {
        currentInput.setLength(0);
        isInputComplete = false;
    }
    
    public void pressCancel() {
        currentInput.setLength(0);
        isInputComplete = false;
    }
    
    public String getPIN() {
        if (isInputComplete) {
            String pin = currentInput.toString();
            reset();
            return pin;
        }
        return null;
    }
    
    public boolean isInputComplete() {
        return isInputComplete;
    }
    
    public int getCurrentInputLength() {
        return currentInput.length();
    }
    
    public String getMaskedInput() {
        return "*".repeat(currentInput.length());
    }
    
    private void reset() {
        currentInput.setLength(0);
        isInputComplete = false;
    }
}