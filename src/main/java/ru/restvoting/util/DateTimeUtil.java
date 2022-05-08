package ru.restvoting.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeUtil {

    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);


    public static LocalDate setStartDate(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }

    public static LocalDate setEndDate(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }

    public static LocalTime getLocalTime() {
        return LocalTime.now();
    }
}
