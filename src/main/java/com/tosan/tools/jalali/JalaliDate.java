package com.tosan.tools.jalali;

import java.io.Serializable;
import java.util.Formatter;
import java.util.TimeZone;

/**
 * Instant of {@link JalaliDate} class preserves the state of date , time and time zone.
 * TimeZone example : GMT+3:30, GMT-13:00, ...
 *
 * @author mosidev
 * @since 9/30/2023
 */
public class JalaliDate implements Serializable, Comparable<JalaliDate> {
    private static final long serialVersionUID = 1603974938502466485L;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;
    private TimeZone timeZone;

    public JalaliDate() {
    }

    public JalaliDate(int year, int month, int day) {
        this(year, month, day, 0, 0, 0, 0, TimeZone.getDefault());
    }

    public JalaliDate(int year, int month, int day, int hour, int minute, int second) {
        this(year, month, day, hour, minute, second, 0, TimeZone.getDefault());
    }

    public JalaliDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        this(year, month, day, hour, minute, second, millisecond, TimeZone.getDefault());
    }

    public JalaliDate(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timeZone) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
        if (timeZone == null)
            this.timeZone = TimeZone.getDefault();
        else
            this.timeZone = timeZone;
    }

    /**
     * Checks for equality with another object.
     *
     * @param o other object
     * @return true if both objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        JalaliDate that = (JalaliDate) o;
        if (this.year != that.year)
            return false;
        if (this.month != that.month)
            return false;
        if (this.day != that.day)
            return false;
        if (this.hour != that.hour)
            return false;
        if (this.minute != that.minute)
            return false;
        if (this.second != that.second)
            return false;
        if (this.timeZone != null && that.timeZone != null) {
            return this.timeZone.getID().equals(that.timeZone.getID());
        }

        return true;
    }

    /**
     * @return hash code for this instance
     */
    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        result = 31 * result + hour;
        result = 31 * result + minute;
        result = 31 * result + second;
        return result;
    }

    @Override
    public int compareTo(JalaliDate o) {
        if (!isValid(this) || !isValid(o)) {
            throw new IllegalArgumentException("invalid date argument");
        }
        if (this.year > o.year) {
            return 1;
        } else if (this.year < o.year) {
            return -1;
        } else {
            if (this.month > o.month) {
                return 1;
            } else if (this.month < o.month) {
                return -1;
            } else {
                if (this.day > o.day) {
                    return 1;
                } else if (this.day < o.day) {
                    return -1;
                } else {
                    if (this.hour > o.hour) {
                        return 1;
                    } else if (this.hour < o.hour) {
                        return -1;
                    } else {
                        if (this.minute > o.minute) {
                            return 1;
                        } else if (this.minute < o.minute) {
                            return -1;
                        } else {
                            if (this.second > o.second) {
                                return 1;
                            } else if (this.second < o.second) {
                                return -1;
                            } else {
                                return Integer.compare(this.millisecond, o.millisecond);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        Formatter formatter = new Formatter();
        return formatter.format("%1$04d/%2$02d/%3$02d %4$02d:%5$02d:%6$02d", year, month, day, hour, minute, second).toString();
    }

    public boolean isValid() {
        return isValid(this);
    }

    private boolean isValid(JalaliDate o) {
        if (!(0 <= o.hour && o.hour < 24)) {
            return false;
        }
        if (!(0 <= o.minute && o.minute < 60)) {
            return false;
        }
        if (!(0 <= o.second && o.second < 60)) {
            return false;
        }
        if (o.year == 0 && o.month == 0 && o.day == 0 && o.hour == 0 && o.minute == 0 && o.second == 0) {
            return false;
        }
        if (!(1 <= o.month && o.month <= 12)) {
            return false;
        }
        if (!(1 <= o.day && o.day <= 31)) {
            return false;
        }
        if (o.year < 0) {
            return false;
        }
        return (o.month <= 6) || (o.month < 12 && o.day <= 30) || (o.month == 12 && (o.day < 30 ||
                (o.day == 30 && JalaliUtil.isLeapYear(year))));
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public int getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(int millisecond) {
        this.millisecond = millisecond;
    }
}
