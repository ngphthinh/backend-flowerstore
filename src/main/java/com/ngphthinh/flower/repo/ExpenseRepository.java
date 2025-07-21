package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.Expense;
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

    List<Expense> findExpensesByDateBetween(LocalDateTime dateAfter, LocalDateTime dateBefore);
}
