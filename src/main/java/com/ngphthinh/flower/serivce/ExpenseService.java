package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.ExpenseMapper;
import com.ngphthinh.flower.repo.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    public ExpenseResponse createExpense(ExpenseRequest expenseRequest) {
        Expense expense = expenseMapper.toExpense(expenseRequest);

        return expenseMapper.toExpenseResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = getExpenseByIdEntity(id);
        return expenseMapper.toExpenseResponse(expense);
    }

    public ExpenseResponse updateExpense(Long id, ExpenseRequest expenseRequest) {
        Expense expense = getExpenseByIdEntity(id);
        expenseMapper.updateExpenseFromRequest(expenseRequest, expense);
        return expenseMapper.toExpenseResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse deleteExpense(Long id) {
        Expense expense = getExpenseByIdEntity(id);
        expenseRepository.delete(expense);
        return expenseMapper.toExpenseResponse(expense);
    }

    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(expenseMapper::toExpenseResponse)
                .toList();
    }

    public Expense getExpenseByIdEntity(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXPENSE_NOT_FOUND));
    }

    public TotalAmountExpenseResponse getTotalAmountExpenseBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);
        BigDecimal totalAmountExpense = expenseRepository.sumAmountByDateBetween(startDate, endDate).orElse(BigDecimal.ZERO);
        return TotalAmountExpenseResponse.builder()
                .totalAmount(totalAmountExpense)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public List<ExpenseResponse> getExpenseBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        return expenseRepository.findExpensesByDateBetween(startDate, endDate)
                .stream()
                .map(expenseMapper::toExpenseResponse)
                .toList();

    }
}
