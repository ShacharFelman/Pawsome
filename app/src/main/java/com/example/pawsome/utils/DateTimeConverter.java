package com.example.pawsome.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static String longToStringDate(long date) {
        return DateTimeConverter.longToLocalDate(date).format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE));
    }

    public static LocalDateTime longToLocalDateTime(long dateTime) {
        return Instant.ofEpochMilli(dateTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static long localDateTimeToLong(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public static String localDateTimeToStringDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE));
    }

    public static String localDateTimeToStringTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME));
    }

    public static String longToStringTime(long dateTime) {
        return DateTimeConverter.longToLocalDateTime(dateTime).format(DateTimeFormatter.ofPattern(Constants.FORMAT_TIME));
    }



}
