package com.example.pawsome.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static LocalDate longToLocalDate(long date) {
        return Instant.ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static long localDateToLong(LocalDate date) {
        return date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public static String localDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE));
    }

    public static String longToString(long date) {
        return DateTimeConverter.longToLocalDate(date).format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE));
    }
}
