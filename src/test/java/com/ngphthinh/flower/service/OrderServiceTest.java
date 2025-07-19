package com.ngphthinh.flower.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.entity.Order;
import com.ngphthinh.flower.enums.DeliveryMethod;
import com.ngphthinh.flower.repo.OrderRepository;
import com.ngphthinh.flower.serivce.OrderDetailService;
import com.ngphthinh.flower.serivce.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    OrderDetailService orderDetailService;

    String jsonOrder;
    String jsonOrderDetails;

    OrderResponse orderResponse;
    Order order;

    List<OrderDetailResponse> orderDetailResponses;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void initData() {
        jsonOrder = """
                { "customerName": "Nguyễn Văn A", "deliveryMethod": "DELIVERY", "orderDate": "2025-07-14T15:30:00", "note": "Giao hàng trước 5 giờ chiều" }
                """;
        jsonOrderDetails = """
                    [ { "productName": "Cà phê sữa", "price": 45000, "quantity": 2 }, { "productName": "Bánh ngọt", "price": 30000, "quantity": 1 }, { "productName": "Bánh mì", "price": 30000, "quantity": 1 } ]
                """;

        order = Order.builder()
                .orderId(1L)
                .customerName("Nguyễn Văn A")
                .deliveryMethod(DeliveryMethod.DELIVERY.toString())
                .orderDate(LocalDateTime.of(2025, 7, 14, 15, 30, 0))
                .note("Giao hàng trước 5 giờ chiều")
                .totalPrice(180000)
                .build();

        orderDetailResponses = List.of(
                OrderDetailResponse.builder().id(1L).productName("Cà phê sữa").price(45000).quantity(2).subtotal(90000).build(),
                OrderDetailResponse.builder().id(2L).productName("Bánh ngọt").price(30000).quantity(1).subtotal(30000).build(),
                OrderDetailResponse.builder().id(3L).productName("Bánh mì").price(30000).quantity(1).subtotal(30000).build()
        );

        orderResponse = OrderResponse.builder().orderId(1L)
                .customerName("Nguyễn Văn A")
                .deliveryMethod(DeliveryMethod.DELIVERY.toString())
                .orderDate(LocalDateTime.of(2025, 7, 14, 15, 30, 0))
                .note("Giao hàng trước 5 giờ chiều")
                .totalPrice(150000)
                .orderDetailResponses(List.of(
                        OrderDetailResponse.builder().id(1L).productName("Cà phê sữa").price(45000).quantity(2).subtotal(90000).build(),
                        OrderDetailResponse.builder().id(2L).productName("Bánh ngọt").price(30000).quantity(1).subtotal(30000).build(),
                        OrderDetailResponse.builder().id(3L).productName("Bánh mì").price(30000).quantity(1).subtotal(30000).build()
                ))
                .build();
    }

    @Test
    void createOder_validRequest_success() {

//        when(orderDetailService.createOrderDetails(any(), any(), imagesBytes, orderRequest)).thenReturn(orderDetailResponses);
//
//        when(orderRepository.save(any())).thenReturn(order);
//
//        OrderResponse orderRes = orderService.createOrder(jsonOrder, jsonOrderDetails);
//
//        assertEquals(orderRes, orderResponse);
    }
}
