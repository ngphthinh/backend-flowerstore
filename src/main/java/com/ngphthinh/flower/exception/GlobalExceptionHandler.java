package com.ngphthinh.flower.exception;

import com.ngphthinh.flower.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final String MIN_ATTRIBUTE = "min";
    private static final String ID_ATTRIBUTE = "id";
    private static final String PHONE_ATTRIBUTE = "phoneNumber";
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage() + ": " + e.getMessage());
//        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException e) {

        String message = e.getErrorCode().getMessage();

        if (Objects.nonNull(e.getKeyAttribute()) && Objects.nonNull(e.getAttributeValue())) {
            message = mapAttribute(message, e.getKeyAttribute(), e.getAttributeValue());
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(message);
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, String attributeKey, String attributeValue) {
        if (message.contains("{" + attributeKey + "}")) {
            return message.replace("{" + attributeKey + "}", attributeValue);
        }
        return message;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        Throwable rootCause = e.getRootCause();

        ErrorCode errorCode = rootCause instanceof DateTimeParseException ? ErrorCode.INVALID_DATE_FORMAT : ErrorCode.UNCATEGORIZED_EXCEPTION;

        String message = rootCause != null ? ": " + rootCause.getMessage() : "";
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage() + message);
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.INVALID_UNCATEGORIZED;
        String message = e.getMessage();


        Class<?> errorTypeClass = e.getRequiredType();


        if (errorTypeClass == null) {
            errorTypeClass = e.getParameter().getParameterType();
        }

        if (errorTypeClass == LocalDate.class) {
            errorCode = ErrorCode.INVALID_DATE_FORMAT;
        } else if (Number.class.isAssignableFrom(errorTypeClass)) {
            errorCode = ErrorCode.INVALID_NUMBER_FORMAT;
        }

        if (e.getValue() != null) {
            message = String.format("Invalid value '%s' for parameter '%s'", e.getValue(), e.getName());
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage() + ": " + message);
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var fieldError = e.getFieldError();

        if (fieldError == null) {
            return ResponseEntity.status(ErrorCode.INVALID_UNCATEGORIZED.getStatusCode()).body(ApiResponse.<Void>builder()
                    .code(ErrorCode.INVALID_UNCATEGORIZED.getCode())
                    .message(ErrorCode.INVALID_UNCATEGORIZED.getMessage())
                    .build());
        }

        String errorKey = fieldError.getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(errorKey);


        var constraintViolation = e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

        var attribute = constraintViolation.getConstraintDescriptor().getAttributes();

        String message = Objects.nonNull(attribute) ? mapAttribute(errorCode.getMessage(), MIN_ATTRIBUTE, String.valueOf(attribute.get(MIN_ATTRIBUTE))) : errorCode.getMessage();

        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(message)
                .build());
    }


    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<ApiResponse<Void>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.MISSING_REQUEST_PART.getCode());
        apiResponse.setMessage(ErrorCode.MISSING_REQUEST_PART.getMessage() + ": " + e.getRequestPartName());
        return ResponseEntity.status(ErrorCode.MISSING_REQUEST_PART.getStatusCode()).body(apiResponse);
    }
}