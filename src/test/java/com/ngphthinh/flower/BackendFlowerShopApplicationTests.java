package com.ngphthinh.flower;

import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.repo.ExpenseRepository;
import com.ngphthinh.flower.repo.OrderRepository;
import com.ngphthinh.flower.serivce.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ExpenseService expenseService;

    @Test
    void contextLoads() {

        var page = expenseService.getAllExpenseWithPaginate(1, 5);


        System.out.println(PagingResponse.<Expense>builder()
                .page(1)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
//                .content(page.getContent())
                .size(5)
                .build());
    }

}
