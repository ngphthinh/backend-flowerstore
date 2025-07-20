package com.ngphthinh.flower.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.request.CreateOrderDetailRequest;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.entity.Order;
import com.ngphthinh.flower.entity.OrderDetail;
import com.ngphthinh.flower.entity.ProductImage;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.OrderDetailMapper;
import com.ngphthinh.flower.repo.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    private final ObjectMapper objectMapper;

    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailMapper orderDetailMapper;

    private final ImageAsyncService imageAsyncService;

    public OrderDetailService(ObjectMapper objectMapper, OrderDetailRepository orderDetailRepository, OrderDetailMapper orderDetailMapper, ImageAsyncService imageAsyncService) {
        this.objectMapper = objectMapper;
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
        this.imageAsyncService = imageAsyncService;
    }

    public List<OrderDetailResponse> createOrderDetails(Order order, String jsonOrderDetailRequests, List<byte[]> imagesBytes, List<Integer> imageIndexes) {
        try {
            List<CreateOrderDetailRequest> orderDetailRequests = objectMapper.readValue(jsonOrderDetailRequests,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CreateOrderDetailRequest.class));

            List<OrderDetail> orderDetails = new ArrayList<>();

            int imageCursor = 0;
            for (int i = 0; i < orderDetailRequests.size(); i++) {
                CreateOrderDetailRequest odr = orderDetailRequests.get(i);
                OrderDetail orderDetail = orderDetailMapper.toOrderDetail(odr);
                orderDetail.setSubtotal(odr.getPrice() * odr.getQuantity());
                order.addOrderDetail(orderDetail);
                orderDetails.add(orderDetailRepository.save(orderDetail));

                if (imageIndexes.contains(i)) {
                    imageAsyncService.uploadImageForProduct(imagesBytes.get(imageCursor++), orderDetail.getId());
                }
            }
            return orderDetails.stream()
                    .map(orderDetailMapper::toOrderDetailResponse)
                    .toList();
        } catch (IOException e) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }
    }

    public List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderOrderId(orderId);
        return orderDetails.stream().map(orderDetailMapper::toOrderDetailResponse).toList();
    }

    public void deleteOrderDetailByOrderId(Long id) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderOrderIdAndProductImageIdNotNull(id);

        orderDetails.stream()
                .map(OrderDetail::getProductImage)
                .map(ProductImage::getPublicId)
                .forEach(imageAsyncService::deleteImage);
    }

    public OrderDetailResponse createOrderDetails(Order order, String productName, double price, int quantity) {
        OrderDetail orderDetail = OrderDetail.builder()
                .productName(productName)
                .price(price)
                .quantity(quantity)
                .subtotal(price * quantity)
                .productImage(null)
                .order(order)
                .build();
        return orderDetailMapper.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
    }
}
