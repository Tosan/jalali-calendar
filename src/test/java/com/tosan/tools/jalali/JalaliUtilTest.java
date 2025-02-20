package com.tosan.tools.jalali;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JalaliUtilTest {

    @Test
    public void checkCorrectLeapYear() {
        final int[] leapYears = {
                1300, 1304, 1309, 1313, 1317, 1321, 1325, 1329, 1333, 1337, 1342, 1346, 1350, 1354, 1358, 1362, 1366,
                1370, 1375, 1379, 1383, 1387, 1391, 1395, 1399, 1403, 1408, 1412, 1416, 1420, 1424, 1428, 1432, 1436,
                1441, 1445, 1449, 1453, 1457, 1461, 1465, 1469, 1474, 1478, 1482, 1486, 1490, 1494, 1498};
        for (int year : leapYears) {
            assertTrue(JalaliUtil.isLeapYear(year));
        }
    }

    @Test
    public void checkCorrectCommonYear() {
        final int[] leapYears = {1400, 1401, 1402, 1404, 1405, 1406, 1407, 1419, 1411, 1413, 1437, 1470};
        for (int year : leapYears) {
            assertFalse(JalaliUtil.isLeapYear(year));
        }
    }

    @Test
    public void convertGregorianToJalali() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.MARCH, 20, 23, 50, 3);
        JalaliDate newJalaliDate = JalaliUtil.gregorianToJalali(calendar.getTime());
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 30, 23, 50, 3);
        assertEquals(jalaliDate, newJalaliDate);
    }

    @Test
    public void convertJalaliToGregorian() {
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 30, 23, 20, 3);
        Date gDate = JalaliUtil.jalaliToGregorian(jalaliDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        assertEquals("2009/03/20 23:20:03", formatter.format(gDate));
    }

    @Test
    public void parse() {
        JalaliDate jalaliDate = new JalaliDate(1383, 9, 19, 15, 14, 0);
        JalaliDate newJalaliDate = JalaliUtil.parseJalaliDate("1383/9/19, 15:14", "yyyy/MM/dd, HH:mm");
        assertEquals(jalaliDate, newJalaliDate);
    }
}
