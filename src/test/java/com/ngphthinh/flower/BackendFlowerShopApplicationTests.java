package com.ngphthinh.flower;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BackendFlowerShopApplicationTests {


    @Test
    void contextLoads() throws JsonProcessingException {

        String json = """
                [1,3,4,5,6,7]
                """;

        ObjectMapper objectMapper = new ObjectMapper();

        List<Integer> myInts = objectMapper
                .readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
        assertEquals(myInts, List.of(1, 3, 4, 5, 6, 7));
    }

}
