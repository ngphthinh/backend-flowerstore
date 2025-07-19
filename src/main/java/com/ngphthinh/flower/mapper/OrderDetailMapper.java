package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.CreateOrderDetailRequest;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.entity.OrderDetail;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "subtotal", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "order", ignore = true)
//    @Mapping(target = "productImage", ignore = true)
    OrderDetail toOrderDetail(CreateOrderDetailRequest orderDetailRequest);

    @Mapping(target = "url", ignore = true)
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    @AfterMapping
    default void setUrl(OrderDetail orderDetail, @MappingTarget OrderDetailResponse response) {
        if (orderDetail.getProductImage() != null) {
            response.setUrl(orderDetail.getProductImage().getUrl());
        } else {
            response.setUrl(null); // hoặc "" tùy bạn
        }
    }
}
