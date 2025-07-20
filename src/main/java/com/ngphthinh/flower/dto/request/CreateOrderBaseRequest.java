package com.ngphthinh.flower.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderBaseRequest {
    private Long storeId;
    private String productName;
    private double price;
    private String note;
    private int quantity;
}
