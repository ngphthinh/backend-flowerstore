package com.ngphthinh.flower.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalPriceOrderResponse {
    private BigDecimal totalPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
