package dev.parhamziaei.teahub.utils;

import com.github.mfathi91.time.PersianDate;
import com.github.mfathi91.time.PersianMonth;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

public final class PersianPeriod {

    private PersianPeriod() {
    }


    public static TimeRange today() {
        PersianDate now = PersianDate.now();

        PersianDate start = PersianDate.of(
                now.getYear(),
                now.getMonth(),
                now.getDayOfMonth()
        );

        return range(
                start,
                start.plusDays(1)
        );
    }


    public static TimeRange yesterday() {
        PersianDate today = PersianDate.now();

        PersianDate start =
                PersianDate.fromGregorian(
                        PersianDate.of(
                                today.getYear(),
                                today.getMonth(),
                                today.getDayOfMonth()
                        ).toGregorian().minusDays(1)
                );

        return range(
                start,
                start.plusDays(1)
        );
    }


    public static TimeRange thisMonth() {
        PersianDate now = PersianDate.now();

        PersianDate start = PersianDate.of(
                now.getYear(),
                now.getMonth(),
                1
        );

        return range(
                start,
                start.plusMonths(1)
        );
    }


    public static TimeRange lastMonth() {
        PersianDate now = PersianDate.now();

        PersianDate currentMonth = PersianDate.of(
                now.getYear(),
                now.getMonth(),
                1
        );

        PersianDate start = PersianDate.fromGregorian(
                currentMonth.toGregorian().minusMonths(1)
        );

        return range(
                start,
                currentMonth
        );
    }


    public static TimeRange thisWeek() {
        PersianDate today = PersianDate.now();

        int diff = today.getDayOfWeek().getValue() - DayOfWeek.SATURDAY.getValue();

        if (diff < 0) {
            diff += 7;
        }

        PersianDate start = PersianDate.fromGregorian(
                today.toGregorian().minusDays(diff)
        );

        return range(
                start,
                start.plusDays(7)
        );
    }


    public static TimeRange lastWeek() {
        TimeRange thisWeek = thisWeek();

        PersianDate start = PersianDate.fromGregorian(
                thisWeek.start().toLocalDate().minusWeeks(7)
        );

        return range(
                start,
                start.plusDays(7)
        );
    }


    public static TimeRange thisYear() {
        PersianDate now = PersianDate.now();

        PersianDate start = PersianDate.of(
                now.getYear(),
                PersianMonth.FARVARDIN,
                1
        );

        return range(
                start,
                start.plusYears(1)
        );
    }


    public static TimeRange lastYear() {
        PersianDate now = PersianDate.now();

        PersianDate start = PersianDate.of(
                now.getYear() - 1,
                PersianMonth.FARVARDIN,
                1
        );

        PersianDate end = PersianDate.of(
                now.getYear(),
                PersianMonth.FARVARDIN,
                1
        );

        return range(
                start,
                end
        );
    }


    private static TimeRange range(
            PersianDate start,
            PersianDate end
    ) {
        return new TimeRange(
                start.toGregorian().atStartOfDay(),
                end.toGregorian().atStartOfDay()
        );
    }


    public record TimeRange(
            LocalDateTime start,
            LocalDateTime end
    ) {
    }
}