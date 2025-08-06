package com.ngphthinh.flower;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.ngphthinh.flower.repo.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    StoreRepository repository;

    @Test
    void contextLoads() throws JsonProcessingException {

        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 31, 23, 59);
    }
}
