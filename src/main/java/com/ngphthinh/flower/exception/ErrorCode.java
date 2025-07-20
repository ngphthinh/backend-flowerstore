package com.ngphthinh.flower.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_DETAIL_NOT_FOUND(1001, "Order detail not found", HttpStatus.BAD_REQUEST),
    IMAGE_UPLOAD_FAILED(1002, "Image upload failed ", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_IMAGE_FAILED(1003, "Delete upload failed ", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPER_ERROR(1004, "Error while mapping DTO to Entity", HttpStatus.INTERNAL_SERVER_ERROR),
    STORE_NOT_FOUND(1005, "Store not found by id {} ", HttpStatus.BAD_REQUEST),
    EXPENSE_NOT_FOUND(1006, "Expense not found by id {} ", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
