package com.ngphthinh.flower.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {
    CREATE_ORDER_SUCCESS(201, "Order created successfully"),
    GET_ORDER_LIST(200, "Order list retrieved successfully"),
    GET_ORDER(200, "Order retrieved successfully"),
    DELETE_ORDER(200, "Order deleted successfully"),

    GET_STORE(200, "Store retrieved successfully"),
    GET_STORE_LIST(200, "Store list retrieved successfully"),
    CREATE_STORE(200, "Store created successfully"),
    DELETE_STORE(200, "Store deleted successfully"),
    UPDATE_STORE(200, "Store updated successfully"),
    ;


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
