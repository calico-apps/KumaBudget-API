package com.calicoapps.kumabudget.budgetmodel;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    public static boolean isOverlap(LocalDateTime start1, LocalDateTime end1,
                                    LocalDateTime start2, LocalDateTime end2) {
        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    public static List<LocalDateTime> generateStartDateTimesWithInterval(LocalDateTime start, LocalDateTime end, Duration interval) {
        List<LocalDateTime> dateTimeSeries = new ArrayList<>();
        dateTimeSeries.add(start);

        LocalDateTime current = start;
        while (current.plus(interval).isBefore(end) || current.plus(interval).isEqual(end)) {
            current = current.plus(interval);
            if (current.isBefore(end) || current.isEqual(end)) {
                dateTimeSeries.add(current);
            }
        }

        if (dateTimeSeries.get(dateTimeSeries.size() - 1).plus(interval).isAfter(end)) {
            dateTimeSeries.remove(dateTimeSeries.size() - 1);
        }

        return dateTimeSeries;
    }

    public static List<LocalDate> getDatesForMonthAndYear(int year, int month) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate start = LocalDate.of(year, month, 1);
        int lengthOfMonth = start.lengthOfMonth();

        for (int i = 0; i < lengthOfMonth; i++) {
            LocalDate date = start.plusDays(i);
            dates.add(date);
        }
        return dates;
    }

}