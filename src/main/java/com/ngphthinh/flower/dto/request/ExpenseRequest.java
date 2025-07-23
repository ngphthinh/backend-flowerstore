package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidDateBeforeToday;
import com.ngphthinh.flower.validator.ValidMoney;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequest {

    @NotBlank(message = "INVALID_TITLE_EXPENSE")
    private String title;

    @ValidMoney(message = "INVALID_AMOUNT_EXPENSE")
    private double amount;

    @ValidDateBeforeToday(message = "INVALID_DATE_EXPENSE")
    private LocalDateTime date;
}
