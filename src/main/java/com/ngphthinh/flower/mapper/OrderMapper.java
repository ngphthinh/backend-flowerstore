package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.CreateOrderRequest;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "store", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "orderId", ignore = true)
//    @Mapping(target = "totalPrice", ignore = true)
    Order toOrder(CreateOrderRequest orderRequest);

    @Mapping(target = "orderDetailResponses", ignore = true)
    @Mapping(target = "storeName", ignore = true)
    OrderResponse toOrderResponse(Order order);

    @AfterMapping
    default void buildStoreName(Order order, @MappingTarget OrderResponse orderResponse) {
        if (order != null && order.getStore() != null) {
            orderResponse.setStoreName(order.getStore().getName());
        }else {
            orderResponse.setStoreName(null);
        }

    }
}
