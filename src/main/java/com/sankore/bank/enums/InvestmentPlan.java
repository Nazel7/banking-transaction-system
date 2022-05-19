package com.sankore.bank.enums;

public enum InvestmentPlan {
    BRONZE(10_000.00, 1.00),
    SILVER(50_000.00, 2.0),
    GOLD(100_000.00, 3.0);

    private final double minAmount;
    private final double intRateMonth;

    InvestmentPlan(Double minAmount, Double intRateMonth) {
        this.minAmount = minAmount;
        this.intRateMonth = intRateMonth;
    }

    public static InvestmentPlan getInvestmentPlan(String investmentPlan) {
        for (InvestmentPlan type: InvestmentPlan.values()) {
            if (type.name().equalsIgnoreCase(investmentPlan)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid InvestmentPlan");
    }

    public double getMinAmount() {
        return minAmount;
    }

    public double getIntRateMonth() {
        return intRateMonth;
    }
}
