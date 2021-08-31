package com.wayapaychat.bank.enums;

public enum TierLevel {

    LEVEL_ONE(100_000.00D),
    LEVEL_TWO(5_000_000.00D),
    LEVEL_THREE(9_999_999_999.99D);

    private final double maxAmount;

    TierLevel(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }
}
