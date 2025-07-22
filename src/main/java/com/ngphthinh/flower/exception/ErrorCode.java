package com.ngphthinh.flower.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {


    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_DETAIL_NOT_FOUND(1001, "Order detail not found by id {id}", HttpStatus.BAD_REQUEST),
    IMAGE_UPLOAD_FAILED(1002, "Image upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_IMAGE_FAILED(1003, "Delete upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPER_ERROR(1004, "Error while mapping DTO to Entity", HttpStatus.INTERNAL_SERVER_ERROR),
    STORE_NOT_FOUND(1005, "Store not found by id {id}", HttpStatus.BAD_REQUEST),
    EXPENSE_NOT_FOUND(1006, "Expense not found by id {id}", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT(1007, "Invalid date format. Please use yyyy-MM-dd", HttpStatus.BAD_REQUEST),
    INVALID_PAGINATION(1008, "Invalid pagination parameters. Page number and size must be greater than 0", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1009, "Order not found by id {id}", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(1010, "Start date must be before end date", HttpStatus.BAD_REQUEST),
    STORE_ID_NULL(1011, "Store id must not be null", HttpStatus.BAD_REQUEST),
    MISSING_REQUEST_PART(1012, "Please provide the required part", HttpStatus.BAD_REQUEST),
    INVALID_NUMBER_FORMAT(1013, "Invalid number format", HttpStatus.BAD_REQUEST),

    INVALID_UNCATEGORIZED(9998, "Invalid field uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_AMOUNT_EXPENSE(1301, "Amount must be greater than {min}", HttpStatus.BAD_REQUEST),
    INVALID_DATE_EXPENSE(1302, "Date must not be in the future", HttpStatus.BAD_REQUEST),
    INVALID_TITLE_EXPENSE(1303, "Title must not be empty", HttpStatus.BAD_REQUEST),

    INVALID_NAME_STORE(1304, "Store name must not be empty", HttpStatus.BAD_REQUEST),
    INVALID_ADDRESS_STORE(1305, "Store address must not be empty", HttpStatus.BAD_REQUEST),

    INVALID_STORE_ID(1306, "Store id must not be empty", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_NAME(1307, "Product name must not be empty", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_QUANTITY(1308, "Product quantity must be greater than {min}", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICE(1309, "Product price must be greater than {min}", HttpStatus.BAD_REQUEST),

    INVALID_CUSTOMER_NAME(1310, "Customer name must not be empty", HttpStatus.BAD_REQUEST),
    INVALID_DELIVERY_METHOD(1311, "Delivery method doesn't exist", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_DATE(1312, "Order date must not be in the future", HttpStatus.BAD_REQUEST),

    INVALID_START_DATE(1313, "Start date cannot be in the future and cannot be null", HttpStatus.BAD_REQUEST),
    INVALID_END_DATE(1314, "End date cannot be null", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_SIZE(1314, "Page size must be greater than {min}", HttpStatus.BAD_REQUEST),
    INVALID_PAGE(1315, "Page must be greater than {min}", HttpStatus.BAD_REQUEST),


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
