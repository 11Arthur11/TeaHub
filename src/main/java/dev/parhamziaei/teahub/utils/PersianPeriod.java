package dev.parhamziaei.teahub.utils;

import com.github.mfathi91.time.PersianDate;
import com.github.mfathi91.time.PersianMonth;
import dev.parhamziaei.teahub.valueobject.TimeRange;

import java.time.LocalTime;

public final class PersianPeriod {

    private PersianPeriod() {
    }

    private static PersianDate minusDays(PersianDate date, long days) {
        return PersianDate.fromGregorian(date.toGregorian().minusDays(days));
    }

    private static PersianDate minusMonths(PersianDate date, long months) {
        return PersianDate.fromGregorian(date.toGregorian().minusMonths(months));
    }

    private static PersianDate minusWeeks(PersianDate date, long weeks) {
        return PersianDate.fromGregorian(date.toGregorian().minusWeeks(weeks));
    }

    private static PersianDate plusDays(PersianDate date, long days) {
        return PersianDate.fromGregorian(date.toGregorian().plusDays(days));
    }

    private static PersianDate plusMonths(PersianDate date, long months) {
        return PersianDate.fromGregorian(date.toGregorian().plusMonths(months));
    }

    public static TimeRange today() {
        PersianDate now = PersianDate.now();
        return range(now, now);
    }

    public static TimeRange yesterday() {
        PersianDate now = PersianDate.now();
        PersianDate yesterday = minusDays(now, 1);
        return range(yesterday, yesterday);
    }

    public static TimeRange thisWeek() {
        PersianDate today = PersianDate.now();

        int dayOfWeek = today.getDayOfWeek().getValue();
        int diff = dayOfWeek - 1;

        PersianDate start = minusDays(today, diff);
        PersianDate end = plusDays(start, 6);

        return range(start, end);
    }

    public static TimeRange lastWeek() {
        TimeRange thisWeek = thisWeek();
        PersianDate start = minusWeeks(
                PersianDate.fromGregorian(thisWeek.start().toLocalDate()),
                1
        );
        PersianDate end = plusDays(start, 6);

        return range(start, end);
    }

    public static TimeRange thisMonth() {
        PersianDate now = PersianDate.now();
        PersianDate start = PersianDate.of(now.getYear(), now.getMonth(), 1);
        PersianDate end = minusDays(plusMonths(start, 1), 1);

        return range(start, end);
    }

    public static TimeRange lastMonth() {
        PersianDate now = PersianDate.now();

        PersianDate currentMonthStart = PersianDate.of(now.getYear(), now.getMonth(), 1);

        PersianDate lastMonthStart = minusMonths(currentMonthStart, 1);

        PersianDate lastMonthEnd = minusDays(currentMonthStart, 1);

        return range(lastMonthStart, lastMonthEnd);
    }

    public static TimeRange thisYear() {
        PersianDate now = PersianDate.now();
        PersianDate start = PersianDate.of(now.getYear(), PersianMonth.FARVARDIN, 1);
        PersianDate end = PersianDate.of(now.getYear(), PersianMonth.ESFAND, 30);

        return range(start, end);
    }

    public static TimeRange lastYear() {
        PersianDate now = PersianDate.now();
        int lastYear = now.getYear() - 1;

        PersianDate start = PersianDate.of(lastYear, PersianMonth.FARVARDIN, 1);
        PersianDate end = PersianDate.of(lastYear, PersianMonth.ESFAND, 30);

        return range(start, end);
    }

    private static TimeRange range(PersianDate start, PersianDate end) {
        return new TimeRange(
                start.toGregorian().atStartOfDay(),
                end.toGregorian().atTime(LocalTime.MAX)
        );
    }

}