# ATM System - Low Level Design Implementation

## Overview

This project implements an ATM (Automated Teller Machine) system using the **State Design Pattern** in Java. The system is designed to be extensible across multiple banks and provides all standard ATM functionalities.

## Features

- **Card Reader**: Support for card insertion, validation, and ejection
- **PIN Interface**: Digital PIN entry with validation and security (3 failed attempts = card block)
- **Cash Dispenser**: Dispenses cash with note minimization algorithm
- **Printer**: Prints transaction receipts and mini statements
- **Mini Statements**: Shows recent transaction history
- **Change PIN**: Secure PIN change functionality
- **Deposit System**: Cash deposit with definite slots
- **Multi-Bank Support**: Extensible architecture for multiple banks

## Architecture

### State Pattern Implementation

The ATM system uses the State pattern to manage different operational states:

```
┌─────────────────┐    insertCard()    ┌──────────────────────┐    enterCorrectPIN()    ┌─────────────────────┐
│   IdleState     │ ────────────────→  │  CardInsertedState   │ ──────────────────────→ │  AuthenticatedState │
│                 │                    │                      │                         │                     │
│ - Welcome       │                    │ - PIN validation     │                         │ - All transactions  │
│ - Insert card   │                    │ - 3 attempts max     │                         │ - Withdraw          │
└─────────────────┘                    │ - Block after fails  │                         │ - Deposit           │
        ↑                              └──────────────────────┘                         │ - Balance inquiry   │
        │                                       │                                       │ - Mini statement    │
        │                              ejectCard() / cancel                            │ - Change PIN        │
        │                                       │                                       └─────────────────────┘
        │                                       ↓                                                │
        └───────────────────────────────────────────────────────────────────────────────────────┘
                                    ejectCard() / cancel / timeout
```

### Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                    ATM System Architecture                                     │
└─────────────────────────────────────────────────────────────────────────────────────────────┘

┌──────────────────┐           ┌─────────────────┐
│   ATMMachine     │◆─────────→│   ATMContext    │
│                  │           │                 │
│ + insertCard()   │           │ + setState()    │
│ + enterPIN()     │           │ + insertCard()  │
│ + withdraw()     │           │ + enterPIN()    │
│ + deposit()      │           │ + withdraw()    │
└──────────────────┘           └─────────────────┘
         │                              │
         │                              │ uses
         ↓                              ↓
┌──────────────────┐           ┌─────────────────┐
│   Hardware       │           │    ATMState     │←─────────────┐
│   Components     │           │   <<interface>> │              │
│                  │           │                 │              │
│ • CardReader     │           │ + insertCard()  │              │
│ • CashDispenser  │           │ + enterPIN()    │              │
│ • Printer        │           │ + withdraw()    │              │
│ • PINPad         │           │ + deposit()     │              │
│ • DepositSlot    │           └─────────────────┘              │
└──────────────────┘                    △                      │
                                        │                      │
                    ┌───────────────────┼──────────────────────┘
                    │                   │
         ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
         │   IdleState     │  │CardInsertedState│  │AuthenticatedState│
         │                 │  │                 │  │                 │
         │ + insertCard()  │  │ + enterPIN()    │  │ + withdraw()    │
         │ + getDisplay()  │  │ + insertCard()  │  │ + deposit()     │
         └─────────────────┘  │ + getDisplay()  │  │ + checkBalance()│
                              └─────────────────┘  │ + changePIN()   │
                                                   └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                     Model Classes                                              │
└─────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      Card       │    │    Account      │    │   Transaction   │    │    CashNote     │
│                 │    │                 │    │                 │    │                 │
│ - cardNumber    │    │ - accountNumber │    │ - transactionId │    │ - denomination  │
│ - pin           │    │ - balance       │    │ - type          │    │ - count         │
│ - bankCode      │    │ - bankCode      │    │ - amount        │    │ + dispense()    │
│ - expiryDate    │    │ + debit()       │    │ - timestamp     │    │ + deposit()     │
│ - isBlocked     │    │ + credit()      │    │ - description   │    └─────────────────┘
│ + isExpired()   │    └─────────────────┘    └─────────────────┘
│ + setBlocked()  │
└─────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                  Bank Service Layer                                            │
└─────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐           ┌─────────────────────────────────────────────┐
│  BankService    │←─────────│            BankServiceFactory               │
│  <<interface>>  │          │                                             │
│                 │          │ + createBankService(bankCode): BankService  │
│ + getAccount()  │          │ + createDefaultBankService(): BankService   │
│ + validateCard()│          └─────────────────────────────────────────────┘
│ + validatePIN() │                           │
│ + updatePIN()   │                           │ creates
│ + processW...() │                           ↓
│ + processD...() │          ┌─────────────────┐    ┌─────────────────────┐
└─────────────────┘          │ ChaseBankService│    │ WellsFargoBankService│
         △                   │                 │    │                     │
         │                   │ - BANK_CODE     │    │ - BANK_CODE         │
         │                   │ - BANK_NAME     │    │ - BANK_NAME         │
         └───────────────────│ + getAccount()  │    │ + getAccount()      │
                             │ + validateCard()│    │ + validateCard()    │
                             └─────────────────┘    └─────────────────────┘
```

## Key Design Patterns Used

### 1. State Pattern
- **Context**: `ATMContext` manages the current state
- **States**: `IdleState`, `CardInsertedState`, `AuthenticatedState`
- **Benefits**: Clean state transitions, easy to add new states

### 2. Strategy Pattern (Bank Services)
- **Interface**: `BankService`
- **Implementations**: `ChaseBankService`, `WellsFargoBankService`
- **Benefits**: Easy to add new banks without modifying existing code

### 3. Factory Pattern
- **Factory**: `BankServiceFactory`
- **Benefits**: Centralized bank service creation

## Cash Dispenser Algorithm

The cash dispenser uses a **greedy algorithm** to minimize the number of notes:

```java
// Supported denominations: $100, $50, $20, $10, $5, $1
// Algorithm: Start with largest denomination and work down
Map<Integer, Integer> calculateMinimumNotes(int amount) {
    // Uses greedy approach to minimize note count
    // Checks availability of each denomination
    // Returns optimal note combination
}
```

## Security Features

1. **PIN Validation**: Maximum 3 attempts before card blocking
2. **Card Expiry Check**: Automatic validation of card expiry date
3. **Card Blocking**: Support for blocked card detection
4. **Transaction Logging**: All transactions are logged with timestamps

## Project Structure

```
src/main/java/com/atm/
├── model/
│   ├── Card.java
│   ├── Account.java
│   ├── Transaction.java
│   └── CashNote.java
├── state/
│   ├── ATMState.java
│   ├── IdleState.java
│   ├── CardInsertedState.java
│   └── AuthenticatedState.java
├── hardware/
│   ├── CardReader.java
│   ├── CashDispenser.java
│   ├── Printer.java
│   ├── PINPad.java
│   └── DepositSlot.java
├── service/
│   ├── BankService.java
│   ├── ChaseBankService.java
│   ├── WellsFargoBankService.java
│   └── BankServiceFactory.java
├── exception/
│   └── InsufficientCashException.java
├── ATMContext.java
├── ATMMachine.java
└── ATMDemo.java
```

## How to Run

1. **Compile the project**:
   ```bash
   javac -d out src/main/java/com/atm/*.java src/main/java/com/atm/*/*.java
   ```

2. **Run the demo**:
   ```bash
   java -cp out com.atm.ATMDemo
   ```

3. **Test with sample cards**:
   - Chase Bank: Card `1234567890123456`, PIN `1234`
   - Chase Bank: Card `9876543210987654`, PIN `5678`

## Extensibility

### Adding a New Bank

1. **Create new bank service**:
   ```java
   public class NewBankService implements BankService {
       // Implement all interface methods
   }
   ```

2. **Update factory**:
   ```java
   public static BankService createBankService(String bankCode) {
       switch (bankCode.toUpperCase()) {
           case "NEWBANK":
               return new NewBankService();
           // ... existing cases
       }
   }
   ```

### Adding New ATM States

1. **Create new state class implementing `ATMState`**
2. **Add state transitions in existing states**
3. **Update `ATMContext` if needed**

## Design Decisions

1. **State Pattern**: Chosen for clean state management and easy extension
2. **Interface Segregation**: Hardware components have focused interfaces
3. **Dependency Injection**: ATM components are injected for testability
4. **Immutable Transactions**: Transaction objects are immutable for audit trail
5. **Note Minimization**: Greedy algorithm for optimal cash dispensing

## Future Enhancements

- Network connectivity for real-time bank communication
- Multi-language support
- Biometric authentication
- Mobile app integration
- Advanced fraud detection
- Receipt customization per bank

## Testing the System

The system includes a console-based demo (`ATMDemo.java`) that allows you to:

1. Insert different bank cards
2. Enter PINs and test validation
3. Perform withdrawals with cash dispensing
4. Make deposits
5. Check balances
6. Print mini statements
7. Change PINs
8. Test security features (card blocking after 3 failed attempts)

### ✅ Verified Test Results

All features have been thoroughly tested and verified:

**Core Functionality:**
- ✅ Card insertion and validation
- ✅ PIN entry with 3-attempt security limit
- ✅ State transitions (Idle → CardInserted → Authenticated)
- ✅ Cash withdrawal with receipt printing
- ✅ Cash deposit with balance updates
- ✅ Balance inquiry with real-time data
- ✅ Mini statement generation
- ✅ PIN change functionality

**Security Features:**
- ✅ Card blocking after 3 failed PIN attempts
- ✅ Insufficient funds protection
- ✅ Proper error handling without data leakage

**Cash Dispensing Algorithm:**
- ✅ Note minimization working (e.g., $285 = 2×$100 + 1×$50 + 1×$20 + 1×$10 + 1×$5)
- ✅ Insufficient cash scenario handling
- ✅ Transaction rollback on dispensing failure

**Sample Test Scenarios:**
```bash
# Test 1: Basic withdrawal
Card: 1234567890123456, PIN: 1234
Action: Withdraw $100
Result: ✅ Success, $100 dispensed, balance updated to $4900

# Test 2: Deposit
Card: 9876543210987654, PIN: 5678  
Action: Deposit $250
Result: ✅ Success, balance updated to $2750

# Test 3: Security test
Card: 1234567890123456
Actions: Enter wrong PIN 3 times (0000, 1111, 2222)
Result: ✅ Card blocked and ejected after 3rd attempt

# Test 4: Insufficient funds
Card: 9876543210987654 (balance $2215)
Action: Withdraw $5000
Result: ✅ "Insufficient funds" message, no deduction
```

Run the demo to see the State pattern in action and test all ATM functionalities.