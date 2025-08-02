package com.ngphthinh.flower.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@Builder
public class ExpenseStatisticsResponse {
    private String date;
    private BigDecimal totalAmount;
    private BigDecimal averageAmount;

    public ExpenseStatisticsResponse(String date, BigDecimal totalAmount, BigDecimal averageAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.averageAmount = averageAmount;
    }
}
