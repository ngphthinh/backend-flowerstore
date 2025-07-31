package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.request.SumAmountExpenseByIdsRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ApiResponse<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        return ApiResponse.<ExpenseResponse>builder()
                .code(ResponseCode.CREATE_EXPENSE.getCode())
                .message(ResponseCode.CREATE_EXPENSE.getMessage())
                .data(expenseService.createExpense(expenseRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ExpenseResponse>> getAllExpenses() {
        return ApiResponse.<List<ExpenseResponse>>builder()
                .code(ResponseCode.GET_EXPENSE_LIST.getCode())
                .message(ResponseCode.GET_EXPENSE_LIST.getMessage())
                .data(expenseService.getAllExpenses())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ExpenseResponse> getExpenseById(@PathVariable("id") Long id) {
        return ApiResponse.<ExpenseResponse>builder()
                .code(ResponseCode.GET_EXPENSE.getCode())
                .message(ResponseCode.GET_EXPENSE.getMessage())
                .data(expenseService.getExpenseById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ExpenseResponse> updateExpense(@PathVariable("id") Long id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        return ApiResponse.<ExpenseResponse>builder()
                .code(ResponseCode.UPDATE_EXPENSE.getCode())
                .message(ResponseCode.UPDATE_EXPENSE.getMessage())
                .data(expenseService.updateExpense(id, expenseRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ExpenseResponse> deleteExpense(@PathVariable("id") Long id) {
        return ApiResponse.<ExpenseResponse>builder()
                .code(ResponseCode.DELETE_EXPENSE.getCode())
                .message(ResponseCode.DELETE_EXPENSE.getMessage())
                .data(expenseService.deleteExpense(id))
                .build();
    }

    @GetMapping("/total")
    public ApiResponse<TotalAmountExpenseResponse> getTotalExpenseBetweenDates(@RequestBody DateRangeRequest dateRangeRequest) {
        return ApiResponse.<TotalAmountExpenseResponse>builder()
                .code(ResponseCode.GET_TOTAL_EXPENSE.getCode())
                .message(ResponseCode.GET_TOTAL_EXPENSE.getMessage())
                .data(expenseService.getTotalAmountExpenseBetweenDates(dateRangeRequest))
                .build();
    }

    @PostMapping("/total/ids")
    public ApiResponse<TotalAmountExpenseResponse> getTotalExpenseByIds(@RequestBody SumAmountExpenseByIdsRequest request) {
        return ApiResponse.<TotalAmountExpenseResponse>builder()
                .code(ResponseCode.GET_TOTAL_EXPENSE.getCode())
                .message(ResponseCode.GET_TOTAL_EXPENSE.getMessage())
                .data(expenseService.getTotalAmountByFilter(request))
                .build();
    }



    @GetMapping("/filter")
    public ApiResponse<PagingResponse<ExpenseResponse>> getExpenseBetweenDates(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "expenseType", required = false) String expenseType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Pageable pageable

    ) {
        return ApiResponse.<PagingResponse<ExpenseResponse>>builder()
                .code(ResponseCode.GET_EXPENSE_BETWEEN_DATE.getCode())
                .message(ResponseCode.GET_EXPENSE_BETWEEN_DATE.getMessage())
                .data(expenseService.getExpensesByFilter(pageable,startDate,endDate, expenseType, searchTerm))
                .build();
    }




}
