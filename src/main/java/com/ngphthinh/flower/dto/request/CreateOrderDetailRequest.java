package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.MinValue;
import com.ngphthinh.flower.validator.ValidMoney;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDetailRequest {


    @NotBlank(message = "INVALID_PRODUCT_NAME")
    private String productName;

    @ValidMoney(message = "INVALID_PRODUCT_PRICE")
    private double price;

    @MinValue(min = 1, message = "INVALID_PRODUCT_QUANTITY")
    @NotNull(message = "INVALID_PRODUCT_QUANTITY")
    private int quantity;
}
