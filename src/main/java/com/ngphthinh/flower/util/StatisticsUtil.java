package com.ngphthinh.flower.util;

import com.ngphthinh.flower.enums.StateStatisticsDay;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class StatisticsUtil {


    private StatisticsUtil() {
    }

    public static DateRange getDateRange(String stateStatisticsDay) {
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (stateStatisticsDay == null || stateStatisticsDay.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);
        }

        StateStatisticsDay range = StateStatisticsDay.valueOf(stateStatisticsDay);

        switch (range) {
            case TODAY -> {
                startDate = LocalDate.now().atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case DAY_7 -> {
                startDate = LocalDate.now().minusDays(7).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case DAY_30 -> {
                startDate = LocalDate.now().minusDays(30).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case DAY_365 -> {
                startDate = LocalDate.now().minusYears(1).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            default ->
                    throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);

        }
        return DateRange.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
