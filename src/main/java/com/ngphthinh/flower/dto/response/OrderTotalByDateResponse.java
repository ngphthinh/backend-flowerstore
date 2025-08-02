package com.ngphthinh.flower.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderTotalByDateResponse {
    private String date;
    private BigDecimal totalAmount;
    private Long totalOrders;

    public OrderTotalByDateResponse(String date, BigDecimal totalAmount, Long totalOrders) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.totalOrders = totalOrders;
    }
}
