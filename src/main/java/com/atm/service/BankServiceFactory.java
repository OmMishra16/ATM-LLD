package com.atm.service;

public class BankServiceFactory {
    
    public static BankService createBankService(String bankCode) {
        switch (bankCode.toUpperCase()) {
            case "CHASE":
                return new ChaseBankService();
            case "WF":
                return new WellsFargoBankService();
            default:
                throw new IllegalArgumentException("Unsupported bank code: " + bankCode);
        }
    }
    
    public static BankService createDefaultBankService() {
        return new ChaseBankService();
    }
}