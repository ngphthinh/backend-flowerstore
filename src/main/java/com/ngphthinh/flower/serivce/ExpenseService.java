package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.enums.StateStatisticsDay;
import com.ngphthinh.flower.util.Constant;
import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.request.SumAmountExpenseByIdsRequest;
import com.ngphthinh.flower.dto.response.*;
import com.ngphthinh.flower.entity.Expense;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.ExpenseMapper;
import com.ngphthinh.flower.repo.ExpenseRepository;
import com.ngphthinh.flower.util.DateRange;
import com.ngphthinh.flower.util.StatisticsUtil;
import com.ngphthinh.flower.validator.PaginateValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Expense getExpenseByIdEntity(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXPENSE_NOT_FOUND, "id", id.toString()));
    }


    public PagingResponse<ExpenseResponse> getExpensesByFilter(Pageable pageable, LocalDate startDate, LocalDate endDate, String expenseType, String searchTerm) {

        PaginateValidator.validatePaginate(pageable);

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

    public TotalAmountExpenseResponse getTotalAmountExpenseByState(String stateStatisticsDay) {

        DateRange dateRange = StatisticsUtil.getDateRange(stateStatisticsDay);

        BigDecimal totalAmountExpense = expenseRepository.sumAmountByDateBetween(dateRange.getStartDate(), dateRange.getEndDate()).orElse(BigDecimal.ZERO);

        return TotalAmountExpenseResponse.builder()
                .totalAmount(totalAmountExpense)
                .startDate(dateRange.getStartDate())
                .endDate(dateRange.getEndDate())
                .build();
    }


    public List<ExpenseStatisticsResponse> getTotalExpenseByState(String stateStatisticsDay) {
        if (stateStatisticsDay == null || stateStatisticsDay.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);
        }

        StateStatisticsDay range = StateStatisticsDay.valueOf(stateStatisticsDay);

        List<ExpenseStatisticsResponse> totalByExpenseDate;

        if (range.equals(StateStatisticsDay.TODAY) || range.equals(StateStatisticsDay.DAY_7) || range.equals(StateStatisticsDay.DAY_30)) {
            LocalDateTime startDate = LocalDate.now().minusDays(7).atStartOfDay();
            LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);

            totalByExpenseDate = mapMissingDates(expenseRepository.findExpenseStatisticsByDate(startDate, endDate));
        } else if (range.equals(StateStatisticsDay.DAY_365)) {
            totalByExpenseDate = mapMissingQuarter( expenseRepository.findExpenseStatisticsQuarter(LocalDateTime.now()));
        } else {
            throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);
        }
        return totalByExpenseDate;
    }

    private List<ExpenseStatisticsResponse> mapMissingQuarter(List<ExpenseStatisticsResponse> totalByExpenseDate) {
        Map<String, ExpenseStatisticsResponse> existingMap = totalByExpenseDate.stream()
                .collect(Collectors.toMap(ExpenseStatisticsResponse::getDate, Function.identity()));
        String []  quarters = {"1", "2", "3", "4"};
        return Stream.of(quarters).map(
                quarter -> {
                    ExpenseStatisticsResponse existing = existingMap.get(quarter);
                    if (existing != null) {
                        return existing;
                    } else {
                        return ExpenseStatisticsResponse.builder()
                                .date(quarter)
                                .totalAmount(BigDecimal.ZERO)
                                .averageAmount(BigDecimal.ZERO)
                                .build();
                    }
                }
        ).toList();
    }


    private List<ExpenseStatisticsResponse> mapMissingDates(List<ExpenseStatisticsResponse> totalByExpenseDate) {
        Map<LocalDate, ExpenseStatisticsResponse> existingMap = totalByExpenseDate.stream()
                .collect(Collectors.toMap(date -> LocalDate.parse(date.getDate()), Function.identity()));

        List<LocalDate> last7Days = Stream.iterate(LocalDate.now().minusDays(6), date -> date.plusDays(1))
                .limit(7)
                .toList();
        return last7Days .stream().map(
                date -> {
                    ExpenseStatisticsResponse existing  = existingMap.get(date);
                    if (existing != null) {
                        return ExpenseStatisticsResponse.builder()
                                .date(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag(Constant.VIETNAMESE)))
                                .totalAmount(existing.getTotalAmount())
                                .averageAmount(existing.getAverageAmount())
                                .build();
                    } else {
                        return ExpenseStatisticsResponse.builder()
                                .date(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag(Constant.VIETNAMESE)))
                                .totalAmount(BigDecimal.ZERO)
                                .averageAmount(BigDecimal.ZERO)
                                .build();
                    }
                }
        ).toList();
    }
}
