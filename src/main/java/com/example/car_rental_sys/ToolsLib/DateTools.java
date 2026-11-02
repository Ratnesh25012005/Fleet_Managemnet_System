package com.example.car_rental_sys.ToolsLib;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Date and timestamp formatting utilities matching original application behavior.
 */
public class DateTools {

    public static String formatDateTime(Date date) {
        if (date == null) date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        Month month = localDate.getMonth();
        String shortMonth = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String formatted = dateFormat.format(date);
        String day = formatted.substring(8, 10);
        String time = formatted.substring(12, 17);
        String ampm = (Integer.parseInt(time.substring(0, 2)) >= 12) ? "PM" : "AM";

        return shortMonth + " " + day + ", " + time + " " + ampm;
    }

    public static String getSpecFormatDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return "2026-11-01";
        }
        return dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
    }

    public static String timeStampToDateTime(long timestamp) {
        if (timestamp == 0) timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    public static long dateTimeToTimestamp(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(dateStr);
            return d.getTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    public static boolean validateDate(String start, String end) {
        try {
            long s = dateTimeToTimestamp(start);
            long e = dateTimeToTimestamp(end);
            return e > s;
        } catch (Exception ignored) {
            return true;
        }
    }

    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
