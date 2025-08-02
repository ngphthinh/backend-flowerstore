package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.dto.response.ExpenseStatisticsResponse;
import com.ngphthinh.flower.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.date BETWEEN :startDate and :endDate")
    Optional<BigDecimal> sumAmountByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
    SELECT e FROM Expense e
    WHERE (e.date BETWEEN :startDate AND :endDate)
      AND (:expenseType IS NULL OR e.expenseType = :expenseType)
      AND (
            :searchTerm IS NULL
            OR UPPER(e.title) LIKE UPPER(CONCAT('%', :searchTerm, '%'))
            OR UPPER(e.expenseType) LIKE UPPER(CONCAT('%', :searchTerm, '%'))
          )
    """)
    Page<Expense> findAllByFilter(LocalDateTime startDate, LocalDateTime endDate, String expenseType, String searchTerm, Pageable pageable);

    @Query("""
    SELECT sum(e.amount) FROM Expense e
    WHERE e.id IN :ids
    """)
    BigDecimal sumAmountByFilter(List<Long> ids);

    @Query("""
        select new com.ngphthinh.flower.dto.response.ExpenseStatisticsResponse(
            cast(CAST(e.date AS localdate) as string),
            cast(sum(e.amount) as bigdecimal ),
            cast(avg(e.amount) as bigdecimal))
        from Expense e
        where e.date between :startDate and :endDate
        group by cast(CAST(e.date AS localdate) as string)
        order by cast(CAST(e.date AS localdate) as string)
    """)
    List<ExpenseStatisticsResponse> findExpenseStatisticsByDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        select new com.ngphthinh.flower.dto.response.ExpenseStatisticsResponse(
            Cast(FUNCTION('QUARTER', e.date) as string),
            cast(sum(e.amount) as bigdecimal ),
            cast(avg(e.amount) as bigdecimal))
        from Expense e
        group by Cast(FUNCTION('QUARTER', e.date) as string)
        order by Cast(FUNCTION('QUARTER', e.date) as string)
    """)
    List<ExpenseStatisticsResponse> findExpenseStatisticsQuarter(LocalDateTime now);
}
