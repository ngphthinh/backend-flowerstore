package com.ngphthinh.flower.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ExpenseService expenseService;

    @Autowired
    ObjectMapper objectMapper;

    ExpenseRequest expenseRequest;
    ExpenseResponse expenseResponse;
    DateRangeRequest dateRangeRequest;
    Long expenseId;

    String baseUrl = "/api/v1/expense";

    @BeforeEach
    void initData() {
        expenseRequest = ExpenseRequest.builder()
                .title("Test Expense")
                .date(LocalDateTime.of(LocalDate.of(2020, 11, 13), LocalTime.MIN))
                .amount(100.0)
                .build();

        expenseResponse = ExpenseResponse.builder()
                .id(1L)
                .title("Test Expense")
                .date(LocalDateTime.of(LocalDate.of(2020, 11, 13), LocalTime.MIN))
                .amount(100.0)
                .build();
    }

    @Test
    void testCreateExpense_success() throws Exception {

        String jsonExpenseRequest = objectMapper.writeValueAsString(expenseRequest);
        Mockito.when(expenseService.createExpense(ArgumentMatchers.any())).thenReturn(expenseResponse);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonExpenseRequest))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<ExpenseResponse> response = objectMapper.readValue(content,
                            objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, ExpenseResponse.class));
                    assert response.getCode() == ResponseCode.CREATE_EXPENSE.getCode();
                    assert response.getMessage().equals(ResponseCode.CREATE_EXPENSE.getMessage());
                    assert response.getData().equals(expenseResponse);
                });
    }


}
