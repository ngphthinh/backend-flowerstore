package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.ExpenseMapper;
import com.ngphthinh.flower.repo.ExpenseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new AppException(ErrorCode.EXPENSE_NOT_FOUND, "id", id.toString()));
    }

    public TotalAmountExpenseResponse getTotalAmountExpenseBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        BigDecimal totalAmountExpense = expenseRepository.sumAmountByDateBetween(startDate, endDate).orElse(BigDecimal.ZERO);
        return TotalAmountExpenseResponse.builder()
                .totalAmount(totalAmountExpense)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public PagingResponse<ExpenseResponse> getExpenseBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        int page = dateRangeRequest.getPage();
        int size = dateRangeRequest.getSize();

        if (size < 1 || page < 1) {
            throw new AppException(ErrorCode.INVALID_PAGINATION);
        }

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("date").descending());
        var expensesPage = expenseRepository.findExpensesByDateBetween(startDate, endDate, pageable);

        List<ExpenseResponse> expenseResponses = expensesPage.getContent().stream()
                .map(expenseMapper::toExpenseResponse)
                .toList();

        return PagingResponse.<ExpenseResponse>builder()
                .content(expenseResponses)
                .page(page)
                .size(size)
                .totalElements(expensesPage.getTotalElements())
                .totalElements(expensesPage.getTotalElements())
                .build();
    }

    public PagingResponse<ExpenseResponse> getAllExpenseWithPaginate(int page, int size) {
        if (page < 1 || size < 1) {
            throw new AppException(ErrorCode.INVALID_PAGINATION);
        }
        int pageRaw = page - 1;

        Pageable pageable = PageRequest.of(pageRaw, size, Sort.by("date").descending());

        var pageExpense = expenseRepository.findAll(pageable);

        List<ExpenseResponse> expenseResponses = pageExpense.getContent().stream().map(expenseMapper::toExpenseResponse).toList();

        return PagingResponse.<ExpenseResponse>builder()
                .page(page)
                .size(size)
                .totalPages(pageExpense.getTotalPages())
                .totalElements(pageExpense.getTotalElements())
                .content(expenseResponses)
                .build();
    }
}
