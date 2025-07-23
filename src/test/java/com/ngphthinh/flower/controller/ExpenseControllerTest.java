package com.ngphthinh.flower.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

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

    PagingResponse<ExpenseResponse> pagingResponse;

    TotalAmountExpenseResponse totalAmountExpenseResponse;


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

        dateRangeRequest = DateRangeRequest.builder()
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now())
                .size(10)
                .page(1)
                .build();

        totalAmountExpenseResponse = TotalAmountExpenseResponse.builder()
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        pagingResponse = PagingResponse.<ExpenseResponse>builder()
                .content(List.of(expenseResponse))
                .page(1)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
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
                    assertTrue(response.getCode() == ResponseCode.CREATE_EXPENSE.getCode());
                    assertTrue(response.getMessage().equals(ResponseCode.CREATE_EXPENSE.getMessage()));
                    assertTrue(response.getData().equals(expenseResponse));
                });
    }

    @Test
    void testGetExpenseById_success() throws Exception {
        expenseId = 1L;
        Mockito.when(expenseService.getExpenseById(expenseId)).thenReturn(expenseResponse);

        mockMvc.perform(get(baseUrl + "/" + expenseId))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<ExpenseResponse> response = objectMapper.readValue(content,
                            objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, ExpenseResponse.class));
                    assertTrue(response.getCode() == ResponseCode.GET_EXPENSE.getCode());
                    assertTrue(response.getMessage().equals(ResponseCode.GET_EXPENSE.getMessage()));
                    assertTrue(response.getData().equals(expenseResponse));
                });
    }

    @Test
    void testGetAllExpense_success() throws Exception {
        Mockito.when(expenseService.getAllExpenses()).thenReturn(java.util.List.of(expenseResponse));
        mockMvc.perform(get(baseUrl))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<List<ExpenseResponse>> response = objectMapper.readValue(content,
                            objectMapper.getTypeFactory().constructParametricType(ApiResponse.class,
                                    objectMapper.getTypeFactory().constructParametricType(List.class, ExpenseResponse.class)));
                    assertTrue(response.getCode() == ResponseCode.GET_EXPENSE_LIST.getCode());
                    assertTrue(response.getMessage().equals(ResponseCode.GET_EXPENSE_LIST.getMessage()));
                    assertTrue(response.getData().contains(expenseResponse));
                });
    }

    @Test
    void testDeleteExpense_success() throws Exception {
        expenseId = 1L;
        Mockito.when(expenseService.deleteExpense(expenseId)).thenReturn(expenseResponse);

        mockMvc.perform(delete(baseUrl + "/" + expenseId))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<Void> response = objectMapper.readValue(content,
                            objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Void.class));
                    assertTrue(response.getCode() == ResponseCode.DELETE_EXPENSE.getCode());
                    assertTrue(response.getMessage().equals(ResponseCode.DELETE_EXPENSE.getMessage()));
                });
    }

    @Test
    void testGetTotalExpenseBetweenDates_success() throws Exception {
        Mockito.when(expenseService.getTotalAmountExpenseBetweenDates(dateRangeRequest)).thenReturn(totalAmountExpenseResponse);

        String jsonDateRangeRequest = objectMapper.writeValueAsString(dateRangeRequest);
        mockMvc.perform(get(baseUrl + "/total")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonDateRangeRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(ResponseCode.GET_TOTAL_EXPENSE.getCode()))
                .andExpect(jsonPath("message").value(ResponseCode.GET_TOTAL_EXPENSE.getMessage()))

        ;
    }

    @Test
    void testGetExpensesBetweenDates_success() throws Exception {
        Mockito.when(expenseService.getExpenseBetweenDates(dateRangeRequest)).thenReturn(pagingResponse);


        String jsonDateRangeRequest = objectMapper.writeValueAsString(dateRangeRequest);
        System.out.println(jsonDateRangeRequest);
        mockMvc.perform(get(baseUrl + "/date")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonDateRangeRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(200));
    }

    @Test
    void testGetAllExpenseWithPaginate_success() throws Exception {
        Mockito.when(expenseService.getAllExpenseWithPaginate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
                .thenReturn(pagingResponse);

        mockMvc.perform(get(baseUrl + "/paginate")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(ResponseCode.GET_EXPENSES_PAGINATE.getCode()))
                .andExpect(jsonPath("message").value(ResponseCode.GET_EXPENSES_PAGINATE.getMessage()))
                .andExpect(jsonPath("data.content[0].id").value(expenseResponse.getId()));
    }

    @Test
    void testUpdateExpense_success() throws Exception {
        expenseId = 1L;
        String jsonExpenseRequest = objectMapper.writeValueAsString(expenseRequest);
        Mockito.when(expenseService.updateExpense(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(expenseResponse);

        mockMvc.perform(put(baseUrl + "/" + expenseId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonExpenseRequest))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ApiResponse<ExpenseResponse> response = objectMapper.readValue(content,
                            objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, ExpenseResponse.class));
                    assertTrue(response.getCode() == ResponseCode.UPDATE_EXPENSE.getCode());
                    assertTrue(response.getMessage().equals(ResponseCode.UPDATE_EXPENSE.getMessage()));
                    assertTrue(response.getData().equals(expenseResponse));
                });
    }

}
