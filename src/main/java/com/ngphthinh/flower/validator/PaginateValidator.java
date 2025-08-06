package com.ngphthinh.flower.validator;

import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import org.springframework.data.domain.Pageable;

public class PaginateValidator {


    private PaginateValidator() {
    }

    public static void validatePaginate(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        if (page < 0) {
            throw new AppException(ErrorCode.INVALID_PAGE);
        }

        if (size < 1 || size > 60) {
            throw new AppException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }
}
