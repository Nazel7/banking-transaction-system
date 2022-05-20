package com.sankore.bank.enums;

public enum Currency {
    NGN,
    EURO;

    public static Currency getInvestmentPlan(String currency) {
        for (Currency type: Currency.values()) {
            if (type.name().equalsIgnoreCase(currency)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid InvestmentPlan");
    }

    }
