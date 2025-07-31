package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.request.SumAmountExpenseByIdsRequest;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalAmountExpenseResponse;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.ExpenseMapper;
import com.ngphthinh.flower.repo.ExpenseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class ExpenseService {

    private static final Logger log = LogManager.getLogger(ExpenseService.class);
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

    public PagingResponse<ExpenseResponse> getExpensesByFilter(Pageable pageable, LocalDate startDate, LocalDate endDate, String expenseType, String searchTerm) {

        validatePaginate(pageable);

        validateDateRange(startDate, endDate);
        LocalDateTime startDateTime = Objects.nonNull(startDate) ? startDate.atStartOfDay() : LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime endDateTime = Objects.nonNull(endDate) ? endDate.atTime(LocalTime.MAX) : LocalDate.now().atTime(LocalTime.MAX);

        if (expenseType.isEmpty()) {
            expenseType = null;
        }

        var expensesPage = expenseRepository.findAllByFilter(startDateTime, endDateTime, expenseType, searchTerm, pageable);

        List<ExpenseResponse> expenseResponses = expensesPage.getContent().stream()
                .map(expenseMapper::toExpenseResponse)
                .toList();

        return PagingResponse.<ExpenseResponse>builder()
                .content(expenseResponses)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(expensesPage.getTotalElements())
                .totalPages(expensesPage.getTotalPages())
                .build();
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

    }

    private void validatePaginate(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        if (page < 0) {
            throw new AppException(ErrorCode.INVALID_PAGE);
        }

        if (size < 1 || size > 60) {
            throw new AppException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }


    public TotalAmountExpenseResponse getTotalAmountByFilter(SumAmountExpenseByIdsRequest request) {
        List<Long> ids = request.getExpenseIds();
        if (ids == null || ids.isEmpty()) {
            return TotalAmountExpenseResponse.builder()
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }

        BigDecimal totalAmount = expenseRepository.sumAmountByFilter(ids);
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }

        return TotalAmountExpenseResponse.builder()
                .totalAmount(totalAmount)
                .build();
    }

}
