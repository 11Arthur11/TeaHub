package dev.parhamziaei.teahub.valueobject;

import com.github.mfathi91.time.PersianDate;

import java.time.LocalDateTime;

public record TimeRange(LocalDateTime start, LocalDateTime end) {

    @Override
    public String toString() {
        PersianDate startPersian = PersianDate.fromGregorian(start.toLocalDate());
        PersianDate endPersian = PersianDate.fromGregorian(end.toLocalDate());
        return String.format(
                "TimeRange[start=%d/%02d/%02d, end=%d/%02d/%02d]",
                startPersian.getYear(), startPersian.getMonth().getValue(), startPersian.getDayOfMonth(),
                endPersian.getYear(), endPersian.getMonth().getValue(), endPersian.getDayOfMonth()
        );
    }

}
