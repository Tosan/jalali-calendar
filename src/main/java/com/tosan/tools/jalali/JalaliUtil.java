package com.tosan.tools.jalali;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mosidev
 * @since 9/30/2023
 */
public class JalaliUtil {
    private static final int[] KHAYYAM_TABLE = {5, 9, 13, 17, 21, 25, 29, 34, 38, 42, 46, 50, 54, 58, 62, 67, 71, 75,
            79, 83, 87, 91, 95, 100, 104, 108, 112, 116, 120, 124};

    public static boolean isLeapYear(int year) {
        int dd;
        if (year >= 474) {
            dd = (year - 474) % 128;
            if (dd == 0)
                return true;
        } else {
            dd = (year >= 342) ? (year - 342) : (128 - (374 - year) % 128);
        }

        for (int i = 0; i < 30; i++) {
            if (KHAYYAM_TABLE[i] == dd)
                return true;
        }

        return false;
    }

    public static Date jalaliToGregorian(JalaliDate jalaliDate) {
        if (jalaliDate.isValid()) {
            JalaliCalendar jalaliCalendar = new JalaliCalendar(jalaliDate);
            return jalaliCalendar.getTime();
        } else {
            return null;
        }
    }

    public static JalaliDate gregorianToJalali(Date gregorian) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(gregorian);
        return new JalaliDate(
                jalaliCalendar.get(Calendar.YEAR),
                jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH),
                jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE),
                jalaliCalendar.get(Calendar.SECOND));
    }

    public static JalaliDate parseJalaliDate(String dateText, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            JalaliCalendar jalaliCalendar = new JalaliCalendar();
            format.setCalendar(jalaliCalendar);
            format.parse(dateText);

            return new JalaliDate(
                    String.valueOf(jalaliCalendar.get(Calendar.YEAR)).length() == 2 ? 1400 +
                            jalaliCalendar.get(Calendar.YEAR) : jalaliCalendar.get(Calendar.YEAR),
                    jalaliCalendar.get(Calendar.MONTH) + 1, jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar
                    .get(Calendar.HOUR_OF_DAY), jalaliCalendar.get(Calendar.MINUTE), jalaliCalendar
                    .get(Calendar.SECOND));
        } catch (ParseException e) {
            throw new IllegalArgumentException("the date text " + dateText + " is not valid.");
        }
    }
}
