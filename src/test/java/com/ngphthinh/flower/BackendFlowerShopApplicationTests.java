package com.ngphthinh.flower;

import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.repo.ExpenseRepository;
import com.ngphthinh.flower.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void contextLoads() {

        LocalDateTime startDate = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.now();

        LocalDateTime dateTest = LocalDateTime.of(2025,7,14, 15, 30, 0);

        LocalDate date = LocalDate.of(2025,7,14);

        var orderDate = orderRepository.findByOrderDate(date);

        orderDate.forEach(order -> System.out.println(order.getCustomerName()));


    }

}
