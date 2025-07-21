package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidDateAfterToday;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRangeRequest {

    @ValidDateAfterToday(message = "INVALID_START_DATE")
    @NotNull(message = "INVALID_START_DATE")
    private LocalDate startDate;

    @NotNull(message = "INVALID_END_DATE")
    private LocalDate endDate;

    private int page;

    private int size;
}