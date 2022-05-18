package com.sankore.bank.enums;

public enum AccountType {

    SAVINGS,
    CURRENT,
    DOMICILLIARY;

    public static AccountType getAccountType(String accountType) {
        for (AccountType type: AccountType.values()) {
            if (type.name().equalsIgnoreCase(accountType)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid AccountType");
    }

}

