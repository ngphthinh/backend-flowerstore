package com.ngphthinh.flower;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.entity.User;
import com.ngphthinh.flower.repo.ExpenseRepository;
import com.ngphthinh.flower.repo.OrderRepository;
import com.ngphthinh.flower.repo.UserRepository;
import com.ngphthinh.flower.serivce.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() throws JsonProcessingException {
//        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("hello");
//        System.out.println(user.getRole().getPermissions());

    }

}
