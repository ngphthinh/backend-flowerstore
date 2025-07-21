package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidDateAfterToday;
import com.ngphthinh.flower.validator.ValidDeliveryMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "INVALID_STORE_ID")
    private Long storeId;

    @NotBlank(message = "INVALID_CUSTOMER_NAME")
    private String customerName;

    @ValidDeliveryMethod(message = "INVALID_DELIVERY_METHOD")
    private String deliveryMethod;

    @ValidDateAfterToday(message = "INVALID_ORDER_DATE")
    private LocalDateTime orderDate;
    private String note;

}
