package com.ngphthinh.flower.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private Long storeId;
    private String customerName;
    private String deliveryMethod;
    private LocalDateTime orderDate;
    private String note;

}
