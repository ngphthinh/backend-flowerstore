package com.ngphthinh.flower.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {
    CREATE_ORDER(201, "Order created successfully"),
    GET_ORDER_LIST(200, "Order list retrieved successfully"),
    GET_ORDERS_PAGINATE(200, "Order list retrieved successfully with pagination"),
    GET_ORDER(200, "Order retrieved successfully"),
    DELETE_ORDER(200, "Order deleted successfully"),
    GET_ORDER_BETWEEN_DATE(200, "Order list retrieved successfully between dates"),

    GET_STORE(200, "Store retrieved successfully"),
    GET_STORE_LIST(200, "Store list retrieved successfully"),
    CREATE_STORE(201, "Store created successfully"),
    DELETE_STORE(200, "Store deleted successfully"),
    UPDATE_STORE(200, "Store updated successfully"),


    CREATE_EXPENSE(201, "Expense created successfully"),
    GET_EXPENSE_LIST(200, "Expense list retrieved successfully"),
    GET_EXPENSE(200, "Expense retrieved successfully"),
    DELETE_EXPENSE(200, "Expense deleted successfully"),
    UPDATE_EXPENSE(200, "Expense updated successfully"),
    GET_TOTAL_EXPENSE(200, "Total expense retrieved successfully"),
    GET_EXPENSES_PAGINATE(200, "Expense list retrieved successfully with pagination"),
    GET_EXPENSE_BETWEEN_DATE(200, "Expense list retrieved successfully between dates"),
    ;


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
