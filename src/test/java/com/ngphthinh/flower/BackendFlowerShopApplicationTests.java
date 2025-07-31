package com.ngphthinh.flower;


import com.ngphthinh.flower.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    OrderRepository repository;

    @Test
    void contextLoads() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("orderDate").descending());

        String inputText = "0";

        Page<Long> pageId = repository.findAllByOrCustomerPhoneContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(inputText, pageable);

        System.out.println(pageId.getContent());

    }

}
