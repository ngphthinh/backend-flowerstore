package com.ngphthinh.flower.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.request.CreateOrderRequest;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.entity.Order;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.enums.DeliveryMethod;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.OrderMapper;
import com.ngphthinh.flower.repo.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final StoreService storeService;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    private final OrderMapper orderMapper;

    private final OrderDetailService orderDetailService;

    public OrderService(StoreService storeService, OrderRepository orderRepository, ObjectMapper objectMapper, OrderMapper orderMapper, OrderDetailService orderDetailService) {
        this.storeService = storeService;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
        this.orderDetailService = orderDetailService;
    }

    public OrderResponse createOrder(String jsonOrder, String jsonOrderDetail, List<MultipartFile> images, String jsonArrayImgIndexes) {
        try {
            // Convert data raw
            List<byte[]> imagesBytes = buildImageBytes(images);
            List<Integer> imageIndexes = objectMapper
                    .readValue(jsonArrayImgIndexes, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
            CreateOrderRequest orderRequest = objectMapper.readValue(jsonOrder, CreateOrderRequest.class);

            // Create and save temporary order
            Order order = orderMapper.toOrder(orderRequest);
            order.setStore(storeService.getStoreEntityById(orderRequest.getStoreId()));
            order.setOrderDetails(new ArrayList<>());
            OrderResponse orderResponse = orderMapper.toOrderResponse(orderRepository.save(order));

            // create list order details
            List<OrderDetailResponse> orderDetailResponses = orderDetailService.createOrderDetails(order, jsonOrderDetail, imagesBytes, imageIndexes);

            double totalPrice = orderDetailResponses.stream().map(OrderDetailResponse::getSubtotal)
                    .reduce(0.0, Double::sum);

            // update prices and set data to respond
            orderResponse.setOrderDetailResponses(orderDetailResponses);
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
            orderResponse.setTotalPrice(order.getTotalPrice());
            return orderResponse;
        } catch (IOException e) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(RuntimeException::new);
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.getOrderDetailsByOrderId(id);

        var orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(orderDetailResponses);
        return orderResponse;
    }

    public List<OrderResponse> getAllInvoices() {

        List<OrderResponse> orderResponses = orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse).toList();

        orderResponses.forEach(e ->
                e.setOrderDetailResponses(orderDetailService.getOrderDetailsByOrderId(e.getOrderId()))
        );
        return orderResponses;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse).toList();

    }

    private List<byte[]> buildImageBytes(List<MultipartFile> images) {
        return images.stream()
                .map(image -> {
                    try {
                        return image.getBytes();
                    } catch (IOException e) {
                        throw new AppException(ErrorCode.MAPPER_ERROR);
                    }
                })
                .toList();
    }

    public OrderResponse deleteOrder(Long id) {
        OrderResponse orderResponse = getOrderById(id);
        orderDetailService.deleteOrderDetailByOrderId(id);
        orderRepository.deleteById(id);
        return orderResponse;
    }

    public OrderResponse createOrder(CreateOrderBaseRequest request) {

        Store store = storeService.getStoreEntityById(request.getStoreId());

        Order order = Order.builder()
                .customerName("Khách lẻ")
                .deliveryMethod(DeliveryMethod.PICKUP.toString())
                .orderDate(LocalDateTime.now())
                .note(request.getNote())
                .totalPrice(request.getPrice())
                .store(store)
                .build();

        orderRepository.save(order);
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetails(order, request.getProductName(), request.getPrice(), request.getQuantity());
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(List.of(orderDetailResponse));
        return orderResponse;
    }
}
