package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.ExpenseRequest;
import com.ngphthinh.flower.dto.response.ExpenseResponse;
import com.ngphthinh.flower.entity.Expense;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseResponse toExpenseResponse(Expense expense);

    Expense toExpense(ExpenseRequest expenseRequest);

    @Mapping(target = "date", ignore = true)
    void updateExpenseFromRequest(ExpenseRequest request, @MappingTarget Expense expense);

    @AfterMapping
    default void afterUpdateExpenseFromRequest(ExpenseRequest request, @MappingTarget Expense expense) {
        if (request.getDate() != null) {
            expense.setDate(request.getDate());
        }
    }
}
