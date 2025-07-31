package com.ngphthinh.flower.enums;


public enum ExpenseType {
    SALARY,
    RENT,
    SPENDING,
    MATERIALS,
    OTHER,;

    public static boolean isValid(String type) {
        for (ExpenseType expenseType : ExpenseType.values()) {
            if (expenseType.name().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

}
