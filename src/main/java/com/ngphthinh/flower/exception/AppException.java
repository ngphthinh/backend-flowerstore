package com.ngphthinh.flower.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String attributeValue;

    private final String keyAttribute;


    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        attributeValue = null;
        keyAttribute = null;
    }


    public AppException(ErrorCode errorCode, String keyAttribute, String attributeValue) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.keyAttribute = keyAttribute;
        this.attributeValue = attributeValue;

    }
}
