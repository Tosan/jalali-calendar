package com.tosan.tools.jalali;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mosidev
 * @since 9/30/2023
 */
public class JalaliCalendarTest {

    @Test
    public void currentDate() {
        JalaliCalendar jalaliCalendar = new JalaliCalendar();
        JalaliDate jalaliDate = new JalaliDate(1388, 8, 4, 14, 18, 0);
        //1388/4/16 14:18:XX > 1388/4/16 14:18:00
        JalaliDate newJalaliDate = new JalaliDate(jalaliCalendar.get(Calendar.YEAR), jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE), jalaliCalendar.get(Calendar.SECOND));
        assertEquals(1, newJalaliDate.compareTo(jalaliDate));
    }

    @Test
    public void convertGtoJWithOutTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:30"));
        calendar.set(2009, Calendar.MARCH, 20, 23, 50, 3);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(calendar);
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 30, 23, 50, 3);
        assertEquals(jalaliDate, jalaliCalendar.getJalaliDate());
    }

    @Test
    public void convertGtoJWithTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:30"));
        calendar.set(2009, Calendar.MARCH, 20, 23, 20, 3);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(calendar, TimeZone.getTimeZone("GMT+4:00"));
        JalaliDate newJalaliDate = new JalaliDate(jalaliCalendar.get(Calendar.YEAR), jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE), jalaliCalendar.get(Calendar.SECOND), 0);
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 30, 23, 50, 3, 0);
        assertEquals(jalaliDate, newJalaliDate);
    }

    @Test
    public void convertJToAnotherTimeZone() {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(new JalaliDate(1387, 12, 30, 23, 65, 60, 346));
        JalaliDate newJalaliDate = new JalaliDate(jalaliCalendar.get(Calendar.YEAR), jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE), jalaliCalendar.get(Calendar.SECOND), 0, TimeZone.getTimeZone("GMT+4:00"));
        JalaliDate jalaliDate = new JalaliDate(1388, 1, 1, 0, 6, 0, 0,
                TimeZone.getTimeZone("GMT+4:00"));
        assertEquals(jalaliDate, newJalaliDate);
    }

    @Test
    public void convertGtoJAnotherWay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.MARCH, 20, 23, 20, 3);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(calendar);
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd KK:mm:ss a");
        format.setCalendar(jalaliCalendar);
        assertEquals("1387 12 30 11:20:03 PM", format.format(jalaliCalendar.getTime()));
    }

    @Test
    public void parse1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
        JalaliCalendar jalaliCalendar = new JalaliCalendar(new JalaliDate(1386, 7, 2, 16, 41, 60));
        assertEquals(1, jalaliCalendar.get(Calendar.AM_PM));
        format.setCalendar(jalaliCalendar);
        assertEquals("1386/07", format.format(jalaliCalendar.getTime()));
        assertEquals(2, jalaliCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void calendarSetter() {
        JalaliCalendar cal = new JalaliCalendar(Calendar.getInstance());
        cal.set(1388, 6, 30, 16, 34, 23);
        JalaliDate jalaliDate = new JalaliDate(1388, 7, 30, 16, 34, 23);
        assertEquals(jalaliDate, cal.getJalaliDate());
    }

    @Test
    public void add() {
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 29, 9, 55, 0, 0,
                TimeZone.getDefault());
        JalaliCalendar jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.add(Calendar.YEAR, 0);
        jalaliCalendar.add(Calendar.MONTH, 3);
        jalaliCalendar.add(Calendar.DAY_OF_MONTH, 0);
        JalaliDate newJalaliDate = new JalaliDate(jalaliCalendar.get(Calendar.YEAR), jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE), 0, 0, TimeZone.getDefault());
        JalaliDate jalaliDate1 = new JalaliDate(1388, 3, 29, 9, 55, 0, 0,
                TimeZone.getDefault());
        assertEquals(newJalaliDate, jalaliDate1);

        jalaliCalendar.add(Calendar.YEAR, -3);
        jalaliCalendar.add(Calendar.MONTH, -2);
        jalaliCalendar.add(Calendar.DAY_OF_MONTH, -5);
        newJalaliDate = new JalaliDate(jalaliCalendar.get(Calendar.YEAR), jalaliCalendar.get(Calendar.MONTH) + 1,
                jalaliCalendar.get(Calendar.DAY_OF_MONTH), jalaliCalendar.get(Calendar.HOUR_OF_DAY),
                jalaliCalendar.get(Calendar.MINUTE), 0, 0, TimeZone.getDefault());
        jalaliDate1 = new JalaliDate(1385, 1, 24, 9, 55, 0, 0,
                TimeZone.getDefault());
        assertEquals(newJalaliDate, jalaliDate1); // Be aware of millisecond !
    }

    @Test
    public void addAnOtherWay() {
        JalaliDate jalaliDate = new JalaliDate(1387, 12, 29, 9, 55, 0, 0);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.add(Calendar.YEAR, 0);
        jalaliCalendar.add(Calendar.MONTH, 3);
        jalaliCalendar.add(Calendar.DAY_OF_MONTH, 0);
        jalaliCalendar.add(Calendar.SECOND, 23);

        assertEquals(1388, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(2, jalaliCalendar.get(Calendar.MONTH)); //Month is started from zero
        assertEquals(29, jalaliCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(9, jalaliCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(55, jalaliCalendar.get(Calendar.MINUTE));
        assertEquals(23, jalaliCalendar.get(Calendar.SECOND));
        assertEquals(0, jalaliCalendar.get(Calendar.AM_PM));

        jalaliCalendar.add(Calendar.YEAR, -3);
        jalaliCalendar.add(Calendar.MONTH, -2);
        jalaliCalendar.add(Calendar.DAY_OF_MONTH, -5);
        jalaliCalendar.add(Calendar.HOUR_OF_DAY, -10);
        assertEquals(1385, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(0, jalaliCalendar.get(Calendar.MONTH)); //Month is started from zero
        assertEquals(23, jalaliCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, jalaliCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(11, jalaliCalendar.get(Calendar.HOUR));
        assertEquals(55, jalaliCalendar.get(Calendar.MINUTE));
        assertEquals(23, jalaliCalendar.get(Calendar.SECOND));
        assertEquals(1, jalaliCalendar.get(Calendar.AM_PM));
    }

    @Test
    public void roll() {
        JalaliDate jalaliDate = new JalaliDate(1388, 8, 11, 23, 35, 45, 0);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.YEAR, 3);
        jalaliCalendar.roll(Calendar.MONTH, true);
        jalaliCalendar.roll(Calendar.DAY_OF_MONTH, 20);
        jalaliCalendar.roll(Calendar.SECOND, 69);

        assertEquals(1391, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(9, jalaliCalendar.get(Calendar.MONTH) + 1); //Month is started from zero
        assertEquals(1, jalaliCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(11, jalaliCalendar.get(Calendar.HOUR));
        assertEquals(35, jalaliCalendar.get(Calendar.MINUTE));
        assertEquals(54, jalaliCalendar.get(Calendar.SECOND));
        assertEquals(1, jalaliCalendar.get(Calendar.AM_PM));

        jalaliDate = new JalaliDate(1387, 12, 30, 23, 35, 45, 0);
        jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.YEAR, true);

        assertEquals(1388, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(12, jalaliCalendar.get(Calendar.MONTH) + 1); //Month is started from zero
        assertEquals(29, jalaliCalendar.get(Calendar.DAY_OF_MONTH));

        jalaliDate = new JalaliDate(1386, 12, 29, 6, 39, 45, 0);
        jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.YEAR, true);

        assertEquals(1387, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(12, jalaliCalendar.get(Calendar.MONTH) + 1); //Month is started from zero
        assertEquals(29, jalaliCalendar.get(Calendar.DAY_OF_MONTH));

        jalaliDate = new JalaliDate(1386, 11, 30, 6, 39, 45, 0);
        jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.MONTH, 2);

        assertEquals(1386, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(1, jalaliCalendar.get(Calendar.MONTH) + 1); //Month is started from zero
        assertEquals(29, jalaliCalendar.get(Calendar.DAY_OF_MONTH));

        jalaliDate = new JalaliDate(1386, 11, 30, 6, 39, 45, 0);
        jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.MONTH, -12);
        jalaliCalendar.roll(Calendar.YEAR, true);
        jalaliCalendar.roll(Calendar.MONTH, 11);
        jalaliCalendar.roll(Calendar.MONTH, -6);
        jalaliCalendar.roll(Calendar.DAY_OF_MONTH, 2);
        jalaliCalendar.roll(Calendar.MONTH, true);
        jalaliCalendar.roll(Calendar.YEAR, false);

        assertEquals(1386, jalaliCalendar.get(Calendar.YEAR));
        assertEquals(5, jalaliCalendar.get(Calendar.MONTH) + 1); //Month is started from zero
        assertEquals(31, jalaliCalendar.get(Calendar.DAY_OF_MONTH));

        jalaliDate = new JalaliDate(1383, 3, 18, 6, 39, 45, 0);
        jalaliCalendar = new JalaliCalendar(jalaliDate);
        jalaliCalendar.roll(Calendar.HOUR, -12);
        jalaliCalendar.roll(Calendar.MINUTE, 15);
        jalaliCalendar.roll(Calendar.SECOND, 15);
        jalaliCalendar.roll(Calendar.MINUTE, -6);
        jalaliCalendar.roll(Calendar.HOUR_OF_DAY, 23);

        assertEquals(5, jalaliCalendar.get(Calendar.HOUR));
        assertEquals(48, jalaliCalendar.get(Calendar.MINUTE));
        assertEquals(0, jalaliCalendar.get(Calendar.SECOND));
    }

    @Test
    public void compareDates() {
        assertEquals(0, new JalaliDate(1388, 3, 16, 13, 20, 34)
                .compareTo(new JalaliDate(1388, 3, 16, 13, 20, 34)));
        assertEquals(0, new JalaliDate(1388, 3, 16, 0, 0, 0)
                .compareTo(new JalaliDate(1388, 3, 16, 0, 0, 0)));
        assertEquals(0, new JalaliDate(1388, 3, 16)
                .compareTo(new JalaliDate(1388, 3, 16)));
        assertEquals(0, new JalaliDate(1388, 3, 16, 0, 0, 34)
                .compareTo(new JalaliDate(1388, 3, 16, 0, 0, 34)));

        //Bigger
        assertEquals(1, new JalaliDate(1388, 3, 16, 13, 20, 35)
                .compareTo(new JalaliDate(1388, 3, 16, 13, 20, 34)));
        //Smaller
        assertEquals(-1, new JalaliDate(1388, 3, 16, 0, 0, 0)
                .compareTo(new JalaliDate(1388, 3, 17, 0, 0, 0)));
        //Smaller
        assertEquals(-1, new JalaliDate(1378, 3, 16)
                .compareTo(new JalaliDate(1388, 6, 30)));
        //Bigger
        assertEquals(1, new JalaliDate(1388, 3, 16, 3, 0, 34)
                .compareTo(new JalaliDate(1388, 3, 16, 0, 0, 34)));
    }

    @Test
    public void equality() {
        JalaliDate j1 = new JalaliDate(1387, 9, 8);
        JalaliDate j2 = new JalaliDate(1387, 9, 8, 0, 0, 0);

        assertNotEquals(null, j1);
        assertEquals(j1, j2);
        assertNotEquals(new JalaliDate(1386, 1, 1, 1, 1, 1), j1);
        assertNotEquals(new JalaliDate(1387, 1, 1, 1, 1, 1), j1);
        assertNotEquals(new JalaliDate(1387, 9, 1, 1, 1, 1), j1);
        assertNotEquals(new JalaliDate(1387, 9, 8, 1, 1, 1), j1);
        assertNotEquals(new JalaliDate(1387, 9, 8, 0, 1, 1), j1);
        assertNotEquals(new JalaliDate(1387, 9, 8, 0, 0, 1), j1);
        assertEquals(new JalaliDate(1387, 9, 8, 0, 0, 0), j1);
    }

    @Test
    public void validity() {
        assertTrue(new JalaliDate(1388, 3, 16, 13, 20, 34).isValid());
        assertFalse(new JalaliDate(1388, 3, 16, 28, 20, 34).isValid());
        assertFalse(new JalaliDate(0, 3, 16, 28, 20, 34).isValid());
        assertTrue(new JalaliDate(1388, 12, 26, 0, 20, 0).isValid());
        assertTrue(new JalaliDate(3000, 3, 16, 10, 20, 18).isValid());
    }

    @Test
    public void testHashCode() {
        JalaliDate jalaliDate1 = new JalaliDate(1387, 8, 8, 9, 10, 20);
        JalaliDate jalaliDate2 = new JalaliDate(1387, 8, 8, 9, 10, 20);
        assertEquals(jalaliDate1.hashCode(), jalaliDate2.hashCode());
    }

    @Test
    public void changeTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
        calendar.set(2009, Calendar.MARCH, 20, 23, 20, 3);
        JalaliCalendar jalaliCalendar = new JalaliCalendar(calendar, TimeZone.getTimeZone("GMT+3:30"));

        assertEquals(1387, jalaliCalendar.get(JalaliCalendar.YEAR));
        assertEquals(12 - 1, jalaliCalendar.get(JalaliCalendar.MONTH));
        assertEquals(30, jalaliCalendar.get(JalaliCalendar.DAY_OF_MONTH));
        assertEquals(11, jalaliCalendar.get(JalaliCalendar.HOUR));
        assertEquals(23, jalaliCalendar.get(JalaliCalendar.HOUR_OF_DAY));
        assertEquals(50, jalaliCalendar.get(JalaliCalendar.MINUTE));
        assertEquals(3, jalaliCalendar.get(JalaliCalendar.SECOND));
    }
}
