package com.sankore.bank.enums;

public enum InvestmentPlan {
    BRONZE(10_000.00),
    SILVER(50_000.00),
    GOLD(100_000.00);

    private final double minAmount;

    InvestmentPlan(Double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }
}
