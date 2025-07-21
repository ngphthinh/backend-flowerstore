package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/date")
    public ApiResponse<PagingResponse<ExpenseResponse>> getExpenseBetweenDates(@RequestBody DateRangeRequest dateRangeRequest) {
        return ApiResponse.<PagingResponse<ExpenseResponse>>builder()
                .code(ResponseCode.GET_EXPENSE_BETWEEN_DATE.getCode())
                .message(ResponseCode.GET_EXPENSE_BETWEEN_DATE.getMessage())
                .data(expenseService.getExpenseBetweenDates(dateRangeRequest))
                .build();
    }

    @GetMapping("/paginate")
    public ApiResponse<PagingResponse<ExpenseResponse>> getAllExpenseWithPaginate(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ApiResponse.<PagingResponse<ExpenseResponse>>builder()
                .code(ResponseCode.GET_EXPENSES_PAGINATE.getCode())
                .message(ResponseCode.GET_EXPENSES_PAGINATE.getMessage())
                .data(expenseService.getAllExpenseWithPaginate(page, size))
                .build();
    }

}
