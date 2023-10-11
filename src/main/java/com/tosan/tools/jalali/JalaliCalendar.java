package com.tosan.tools.jalali;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The JalaliCalendar class provides methods for converting specific instant of {@link JalaliDate},
 * {@link Date} or {@link Calendar} to each other.
 *
 * @author mosidev
 * @since 9/30/2023
 */
public class JalaliCalendar extends Calendar {
    private static final long serialVersionUID = 638910237070675779L;
    private static final byte[] GREGORIAN_DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final byte[] JALALI_DAYS_IN_MONTH = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    private final TimeZone desTimeZone;
    private final TimeZone eraTimeZone;
    private boolean calcOverflow;
    private boolean computeDesTimeZone;
    private boolean isCalledConstructor = false;
    private boolean isJalaliDate;

    public JalaliCalendar() {
        this(Calendar.getInstance());
    }

    /**
     * Get {@link JalaliDate} with its timezone.
     *
     * @param jalaliDate jalaliDate
     */
    public JalaliCalendar(JalaliDate jalaliDate) {
        this(jalaliDate, jalaliDate.getTimeZone());
    }

    /**
     * Get {@link JalaliDate} with its timezone and convert to destination timezone.
     *
     * @param jalaliDate  jalaliDate
     * @param desTimeZone desTimeZone
     */
    public JalaliCalendar(JalaliDate jalaliDate, TimeZone desTimeZone) {
        this(jalaliDate, jalaliDate.getTimeZone(), desTimeZone);
    }

    /**
     * @param jalaliDate  jalaliDate
     * @param eraTimeZone era timezone
     * @param desTimeZone destination timezone
     */
    private JalaliCalendar(JalaliDate jalaliDate, TimeZone eraTimeZone, TimeZone desTimeZone) {
        isJalaliDate = true;
        fields[1] = jalaliDate.getYear();
        fields[2] = jalaliDate.getMonth();
        fields[5] = jalaliDate.getDay();
        fields[11] = jalaliDate.getHour();
        fields[12] = jalaliDate.getMinute();
        fields[13] = jalaliDate.getSecond();
        fields[14] = jalaliDate.getMillisecond();
        this.eraTimeZone = eraTimeZone;
        if (desTimeZone == null)
            this.desTimeZone = TimeZone.getDefault();
        else
            this.desTimeZone = desTimeZone;
        if (fields[13] > 59) {
            int remainedSecond = fields[13] - 59;
            fields[13] = 59;
            add(Calendar.SECOND, remainedSecond);
        }
        if (fields[12] > 59) {
            int remainedMinute = fields[12] - 59;
            fields[12] = 59;
            add(Calendar.MINUTE, remainedMinute);
        }
        if (fields[11] > 24) {
            int remainedHour = fields[11] - 24;
            fields[11] = 24;
            add(Calendar.HOUR_OF_DAY, remainedHour);
        }
        int baseDay = fields[5];
        if (fields[2] > 12) {
            int remainedMonth = fields[2] - 12;
            fields[2] = 12;
            add(Calendar.MONTH, remainedMonth);
        }
        if ((fields[2] >= 1 && fields[2] <= 6)) {
            if (baseDay > 31) {
                add(Calendar.DAY_OF_MONTH, baseDay - 31);
            }
        } else if ((fields[2] > 6 && fields[2] <= 11)) {
            if (baseDay > 30) {
                add(Calendar.DAY_OF_MONTH, baseDay - 30);
            }
        } else if (fields[2] == 12) {
            if (JalaliUtil.isLeapYear(fields[1])) {
                if (baseDay > 30) {
                    add(Calendar.DAY_OF_MONTH, baseDay - 30);
                }
            } else {
                if (baseDay > 29) {
                    add(Calendar.DAY_OF_MONTH, baseDay - 29);
                }
            }
        }
        fields[10] = fields[11];
        isTimeSet = false;
        computeDesTimeZone = true;
    }

    /**
     * Get {@link Date} with the default timezone and convert to jalali date and default timezone.
     *
     * @param date date
     */
    public JalaliCalendar(Date date) {
        this(date, TimeZone.getDefault());
    }

    /**
     * Get {@link Date} with the default timezone and convert to jalali date and destination timezone.
     *
     * @param date        date
     * @param desTimeZone destination timezone
     */
    public JalaliCalendar(Date date, TimeZone desTimeZone) {
        this(date, TimeZone.getDefault(), desTimeZone);
    }

    /**
     * Get {@link Date} with the era timezone and convert to jalali date and destination timezone.
     *
     * @param date        date
     * @param eraTimeZone era timezone
     * @param desTimeZone destination timezone
     */
    public JalaliCalendar(Date date, TimeZone eraTimeZone, TimeZone desTimeZone) {
        isJalaliDate = false;
        isCalledConstructor = true;
        if (eraTimeZone == null)
            this.eraTimeZone = TimeZone.getDefault();
        else
            this.eraTimeZone = eraTimeZone;
        if (desTimeZone == null)
            this.desTimeZone = TimeZone.getDefault();
        else
            this.desTimeZone = desTimeZone;
        setTime(date);
    }

    /**
     * Get {@link Calendar} with its timezone and convert to jalali date and the calendar timezone.
     *
     * @param calendar calendar
     */
    public JalaliCalendar(Calendar calendar) {
        this(calendar.getTime(), calendar.getTimeZone());
    }

    /**
     * Get {@link Calendar} with its timezone and convert to jalali date and destination timezone.
     *
     * @param calendar    calendar
     * @param desTimeZone destination timezone
     */
    public JalaliCalendar(Calendar calendar, TimeZone desTimeZone) {
        this(calendar.getTime(), desTimeZone);
    }

    /**
     * Returns the jalali calendar as a {@link JalaliDate} object.
     */
    public JalaliDate getJalaliDate() {
        convertToJalaliDate();
        return new JalaliDate(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH),
                get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), get(Calendar.SECOND));
    }

    private void convertToJalaliDate() {
        if (!isJalaliDate) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(fields[1], fields[2], fields[5], fields[11], fields[12], fields[13]);
            Calendar resultCalendar;
            if (computeDesTimeZone) {
                resultCalendar = Calendar.getInstance();
            } else {
                resultCalendar = Calendar.getInstance(this.desTimeZone);
            }
            resultCalendar.setTime(calendar.getTime());
            convert(resultCalendar);
            isJalaliDate = true;
            this.setTimeZone(desTimeZone);
        }
    }

    /**
     * Returns the Gregorian date and time as a {@link Calendar} object.
     *
     * @return {@link Calendar}
     */
    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        if (fields[1] == 0 || fields[2] == 0 || fields[5] == 0) {
            if (fields[1] == 0) {
                fields[1] = getMinimum(Calendar.YEAR);
            }
            if (fields[2] == 0) {
                fields[2] = 1;
            }
            if (fields[5] == 0) {
                fields[5] = 1;
            }
        }
        int[] result = jalaliToGregorian(fields[1], fields[2], fields[5]);
        calendar.set(result[0], result[1] - 1, result[2], fields[11], fields[12], fields[13]);
        calendar.setTimeZone(this.eraTimeZone);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd KK:mm:ss a zzz yyyy");
        format.setTimeZone(this.desTimeZone);
        format.format(calendar.getTime());
        Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.set(format.getCalendar().get(Calendar.YEAR), format.getCalendar().get(Calendar.MONTH), format
                .getCalendar().get(Calendar.DAY_OF_MONTH), format.getCalendar().get(Calendar.HOUR_OF_DAY), format
                .getCalendar().get(Calendar.MINUTE), format.getCalendar().get(Calendar.SECOND));
        //We need this step for setting the correct TimeZone (desTimeZone).
        //Notice : resultCalendar.setTime(this.desTimeZone) or Calendar resultCalendar = Calendar.getInstance(this.desTimeZone)
        //Does not work properly !
        Calendar correctTZCalendar = Calendar.getInstance(this.desTimeZone);
        correctTZCalendar.setTime(resultCalendar.getTime());
        if (fields[9] == 0) {
            correctTZCalendar.clear(Calendar.AM_PM);
        }
        if (fields[10] == 0) {
            correctTZCalendar.clear(Calendar.HOUR);
        }
        if (fields[11] == 0) {
            correctTZCalendar.clear(Calendar.HOUR_OF_DAY);
        }
        if (fields[12] == 0) {
            correctTZCalendar.clear(Calendar.MINUTE);
        }
        if (fields[13] == 0) {
            correctTZCalendar.clear(Calendar.SECOND);
        }
        if (fields[14] == 0) {
            correctTZCalendar.clear(Calendar.MILLISECOND);
        }
        computeDesTimeZone = true;
        return correctTZCalendar;
    }

    private void convert(Calendar calendar) {
        int[] jalali = gregorianToJalali(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        fields[1] = jalali[0];
        fields[2] = jalali[1];
        fields[5] = jalali[2];
        fields[9] = calendar.get(Calendar.AM_PM);
        fields[10] = calendar.get(Calendar.HOUR);
        fields[11] = calendar.get(Calendar.HOUR_OF_DAY);
        fields[12] = calendar.get(Calendar.MINUTE);
        fields[13] = calendar.get(Calendar.SECOND);
        if (fields[14] != 0) {
            fields[14] = calendar.get(Calendar.MILLISECOND);
        }
    }

    private int[] gregorianToJalali(int... g) {
        int gy;
        int gm;
        int gd;

        int jy;
        int jm;
        int jd;

        int gDays;
        int jDays;
        int jNp;
        int i;

        gy = g[0] - 1600;
        gm = g[1] - 1;
        gd = g[2] - 1;

        gDays = (365 * gy) + ((gy + 3) / 4) - ((gy + 99) / 100) + ((gy + 399) / 400);
        for (i = 0; i < gm; i++) {
            gDays += GREGORIAN_DAYS_IN_MONTH[i];
        }
        if (gm > 1 && (gy % 4 == 0 && gy % 100 != 0 || gy % 400 == 0)) {
            // leap and after Feb
            ++gDays;
        }
        gDays += gd;

        jDays = gDays - 79;
        jNp = jDays / 12053;
        jDays %= 12053;
        jy = 979 + (33 * jNp) + (4 * (jDays / 1461));
        jDays %= 1461;

        if (jDays >= 366) {
            jy += (jDays - 1) / 365;
            jDays = (jDays - 1) % 365;
        }

        fields[6] = jDays + 1;

        for (i = 0; i < 11 && jDays >= JALALI_DAYS_IN_MONTH[i]; i++) {
            jDays -= JALALI_DAYS_IN_MONTH[i];
        }
        jm = i + 1;
        jd = jDays + 1;
        return new int[]{jy, jm, jd};
    }

    private int[] jalaliToGregorian(int... j) {
        int gy;
        int gm;
        int gd;

        int jy;
        int jm;
        int jd;

        int gDays;
        int jDays;
        int leap;
        int i;

        jy = j[0] - 979;
        jm = j[1] - 1;
        jd = j[2] - 1;

        jDays = (365 * jy) + ((jy / 33) * 8) + (((jy % 33) + 3) / 4);
        for (i = 0; i < jm; i++) {
            jDays += JALALI_DAYS_IN_MONTH[i];
        }
        jDays += jd;

        gDays = jDays + 79;
        gy = 1600 + (400 * (gDays / 146097)); // 146097 = 365 * 400 + 400 / 4 - 400 / 100 + 400 / 400
        gDays %= 146097;

        leap = 1;
        if (gDays >= 36525) { //36525 = 365 * 100 + 100 / 4
            gDays--;
            gy += 100 * (gDays / 36524); // 36525 = 365 * 100 + 100 / 4 - 100 / 100
            gDays %= 36524;
            if (gDays >= 365) {
                gDays++;
            } else {
                leap = 0;
            }
        }

        gy += 4 * (gDays / 1461); // 1461 = 365 * 4 + 4 / 4
        gDays %= 1461;

        if (gDays >= 366) {
            leap = 0;
            gDays--;
            gy += gDays / 365;
            gDays %= 365;
        }

        for (i = 0; gDays >= GREGORIAN_DAYS_IN_MONTH[i] + boolToInt(i == 1 && leap == 1); i++) {
            gDays -= GREGORIAN_DAYS_IN_MONTH[i] + boolToInt(i == 1 && leap == 1);
        }
        gm = i + 1;
        gd = gDays + 1;
        return new int[]{gy, gm, gd};
    }

    private int boolToInt(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    private void increment(int iYear, int iMonth, int iDay, boolean calcOverflow) {
        if (!isJalaliDate) {
            convertToJalaliDate();
            this.isJalaliDate = true;
        }

        fields[1] += iYear;
        fields[2] += iMonth;

        fields[1] += (fields[2] % 12 == 0) ? (fields[2] / 12) - 1 : fields[2] / 12;
        fields[2] = (fields[2] % 12 == 0) ? fields[2] % 12 + 12 : fields[2] % 12;

        if ((!calcOverflow) && (fields[5] > daysOfMonth(fields[1], fields[2]))) {
            fields[5] = daysOfMonth(fields[1], fields[2]);
        }
        fields[5] += iDay;

        while (fields[5] > daysOfMonth(fields[1], fields[2])) {
            fields[5] -= daysOfMonth(fields[1], fields[2]);
            fields[2]++;
            if (fields[2] > 12) {
                fields[1]++;
                fields[2] = 1;
            }
        }
    }

    private void decrement(int dYear, int dMonth, int dDay) {
        if (!isJalaliDate) {
            convertToJalaliDate();
            this.isJalaliDate = true;
        }

        boolean bMnthLastDay = false;
        if (fields[5] == daysOfMonth(fields[1], fields[2]))
            bMnthLastDay = true;

        fields[1] -= dYear;
        fields[2] -= dMonth;

        fields[1] -= (fields[2] % 12 < 1) ? -(fields[2] / 12) + 1 : -(fields[2] / 12);
        fields[2] = (fields[2] % 12 < 1) ? fields[2] % 12 + 12 : fields[2] % 12;

        if (fields[5] > daysOfMonth(fields[1], fields[2]) || bMnthLastDay)
            fields[5] = daysOfMonth(fields[1], fields[2]);

        while (dDay >= fields[5]) {
            dDay -= fields[5];

            if (--fields[2] < 1) {
                fields[1]--;

                fields[2] = 12;
            }

            fields[5] = daysOfMonth(fields[1], fields[2]);
        }
        fields[5] -= dDay;
    }

    private int daysOfMonth(int aYear, int aMonth) {
        if (!(0 < aMonth && aMonth < 13))
            throw new RuntimeException("ERROR INVALID MONTH");

        return aMonth <= 6 ? 31 : aMonth < 12 ? 30 : JalaliUtil.isLeapYear(aYear) ? 30 : 29;
    }

    public int get(int field) {
        this.setTimeZone(this.desTimeZone);
        if (!isJalaliDate) {
            convertToJalaliDate();
        }
        complete();
        switch (field) {
            case YEAR:
                return fields[1];
            case MONTH:
                return fields[2] - 1;
            case DAY_OF_MONTH:
                return fields[5];
            case DAY_OF_YEAR:
                return fields[6];
            case DAY_OF_WEEK:
                return fields[7];
            case HOUR:
                return (fields[11] == 0 ? 0 : fields[11] == 12 ? 0 : fields[11] > 12 ? fields[11] % 12 : fields[11]);
            case HOUR_OF_DAY:
                return (fields[11] == 0 ? 0 : fields[11] == 24 ? 0 : fields[11] > 24 ? (fields[11] % 24) : fields[11]);
            case MINUTE:
                return fields[12];
            case SECOND:
                return fields[13];
            case MILLISECOND:
                return fields[14];
            case AM_PM:
                if (fields[11] >= 0 && fields[11] < 12) {
                    if (fields[9] == 0) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (fields[11] >= 12 && fields[11] <= 23) {
                    return 1;
                }
            case ZONE_OFFSET:
                return fields[15];
            case DST_OFFSET:
                return fields[16];
            default:
                return 0;
        }
    }

    public void set(int field, int value) {
        this.setTimeZone(this.desTimeZone);
        switch (field) {
            case YEAR:
                fields[1] = value;
                break;
            case MONTH:
                fields[2] = value + 1;
                break;
            case DAY_OF_MONTH:
                fields[5] = value;
                break;
            case HOUR:
            case HOUR_OF_DAY:
                fields[10] = (value == 0 ? 0 : value == 24 ? 0 : value > 24 ? (value % 24) : value);
                fields[11] = (value == 0 ? 0 : value == 24 ? 0 : value > 24 ? (value % 24) : value);
                break;
            case MINUTE:
                fields[12] = value;
                break;
            case SECOND:
                fields[13] = value;
                break;
            case MILLISECOND:
                fields[14] = value;
                break;
            case AM_PM:
                fields[9] = value == 0 ? AM : PM;
                break;
            default:
                break;
        }
        isTimeSet = false;
    }

    /**
     * Just work fine for YEAR, MONTH, DAY_OF_MONTH, HOUR, HOUR_OF_DAY, MINUTE, SECOND
     * <p>
     * Adds or subtracts the specified amount of time to the given calendar field, based on the calendar's rules.
     * For example, to subtract 5 days from the current time of the calendar, you can achieve it by calling:
     * add(Calendar.DAY_OF_MONTH, -5).
     * Overrides: add(...) in Calendar
     *
     * @param field  field
     * @param amount amount
     */
    public void add(int field, int amount) {
        boolean calcOverflow = isCalcOverflow();
        switch (field) {
            case YEAR:
                if (amount >= 0)
                    increment(amount, 0, 0, calcOverflow);
                else
                    decrement(-amount, 0, 0);
                break;
            case MONTH:
                if (amount >= 0)
                    increment(0, amount, 0, calcOverflow);
                else
                    decrement(0, -amount, 0);
                break;
            case DAY_OF_MONTH:
                if (amount >= 0)
                    increment(0, 0, amount, calcOverflow);
                else
                    decrement(0, 0, -amount);
                break;
            case HOUR:
            case HOUR_OF_DAY:
                fields[11] += amount;
                fields[10] += amount;
                break;
            case MINUTE:
                fields[12] += amount;
                break;
            case SECOND:
                fields[13] += amount;
                break;
            default:
                break;
        }
        isTimeSet = false;
    }

    @Override
    protected void computeFields() {
        Calendar calendar = Calendar.getInstance(eraTimeZone);
        calendar.setTime(new Date(this.time));
        if (isCalledConstructor) {
            fields[1] = calendar.get(Calendar.YEAR);
            fields[2] = calendar.get(Calendar.MONTH);
            fields[5] = calendar.get(Calendar.DAY_OF_MONTH);
            fields[6] = calendar.get(Calendar.DAY_OF_YEAR);
            fields[7] = calendar.get(Calendar.DAY_OF_WEEK);
            fields[9] = calendar.get(Calendar.AM_PM);
            fields[11] = calendar.get(Calendar.HOUR_OF_DAY);
            fields[12] = calendar.get(Calendar.MINUTE);
            fields[13] = calendar.get(Calendar.SECOND);
            fields[14] = calendar.get(Calendar.MILLISECOND);
            fields[15] = calendar.get(Calendar.ZONE_OFFSET);
            fields[16] = calendar.get(Calendar.DST_OFFSET);
            isCalledConstructor = false;
        } else {
            fields[1] = (fields[1] == 0) ? this.getMinimum(Calendar.YEAR) : calendar.get(Calendar.YEAR);
            fields[2] = (fields[2] == 0) ? 0 : calendar.get(Calendar.MONTH);
            fields[5] = (fields[5] == 0) ? 0 : calendar.get(Calendar.DAY_OF_MONTH);
            fields[6] = calendar.get(Calendar.DAY_OF_YEAR);
            fields[7] = calendar.get(Calendar.DAY_OF_WEEK);
            fields[9] = calendar.get(Calendar.AM_PM);
            fields[11] = calendar.get(Calendar.HOUR_OF_DAY);
            fields[12] = calendar.get(Calendar.MINUTE);
            fields[13] = calendar.get(Calendar.SECOND);
            fields[14] = calendar.get(Calendar.MILLISECOND);
            fields[15] = calendar.get(Calendar.ZONE_OFFSET);
            fields[16] = calendar.get(Calendar.DST_OFFSET);
        }
        isJalaliDate = false;
        convertToJalaliDate();
    }

    @Override
    protected void computeTime() {
        this.time = getCalendar().getTime().getTime();
    }

    @Override
    public int getGreatestMinimum(int field) {
        return getMinimum(field);
    }

    @Override
    public int getLeastMaximum(int field) {
        return getMaximum(field);
    }

    @Override
    public int getMaximum(int field) {
        if (!isJalaliDate) {
            convertToJalaliDate();
        }
        if (field == YEAR) {
            return 9979;
        }
        return Calendar.getInstance().getMaximum(field);
    }

    @Override
    public int getMinimum(int field) {
        if (!isJalaliDate) {
            convertToJalaliDate();
        }
        if (field == YEAR) {
            return 979;
        }
        return Calendar.getInstance().getMinimum(field);
    }

    /**
     * Just work fine for YEAR, MONTH, DAY_OF_MONTH, HOUR, HOUR_OF_DAY, MINUTE, SECOND
     * <p>
     * Adds or subtracts (up/down) a single unit of time on the given time
     * field without changing larger fields. For example, to roll the current
     * date up by one day, you can achieve it by calling:
     * <p>roll(Calendar.DATE, true).
     * When rolling on the year or <code>Calendar.YEAR</code> field, it will roll the year
     * value in the range between 1 and the value returned by calling
     * <code>getMaximum(Calendar.YEAR)</code>.
     * When rolling on the month or <code>Calendar.MONTH</code> field, other fields like
     * date might conflict and, need to be changed. For instance,
     * rolling the month on the date 01/31/96 will result in 02/29/96.
     * When rolling on the hour-in-day or Calendar.HOUR_OF_DAY field, it will
     * roll the hour value in the range between 0 and 23, which is zero-based.
     *
     * @param field the time field.
     * @param up    indicates if the value of the specified time field is to be
     *              rolled up or rolled down. Use true if rolling up, false otherwise.
     * @see Calendar#add(int, int)
     * @see Calendar#set(int, int)
     */
    @Override
    public void roll(int field, boolean up) {
        if (!isJalaliDate) {
            convertToJalaliDate();
            this.isJalaliDate = true;
        }
        switch (field) {
            case YEAR:
                fields[1] += up ? +1 : -1;
                boolean leapYear = JalaliUtil.isLeapYear(fields[1]);
                if (!leapYear && fields[2] == 12) {
                    if (fields[5] >= 30) {
                        fields[5] = 29;
                    }
                }
                if (leapYear && fields[2] == 12) {
                    if (fields[5] >= 30) {
                        fields[5] = 30;
                    }
                }
                break;
            case MONTH:
                fields[2] += up ? +1 : -1;
                if (fields[2] > 12) {
                    fields[2] = 1;
                } else if (fields[2] < 1) {
                    fields[2] = 12;
                }
                if (fields[2] > 6) {
                    if (fields[2] == 12) {
                        leapYear = JalaliUtil.isLeapYear(fields[1]);
                        if (!leapYear) {
                            if (fields[5] > 29) {
                                fields[5] = 29;
                            }
                        } else {
                            if (fields[5] > 30) {
                                fields[5] = 30;
                            }
                        }
                    } else if (fields[5] > 30) {
                        fields[5] = 30;
                    }
                }
                break;
            case DAY_OF_MONTH:
                fields[5] += up ? +1 : -1;
                leapYear = JalaliUtil.isLeapYear(fields[1]);
                if (fields[2] >= 1 && fields[2] <= 6) {
                    if (fields[5] < 1) {
                        fields[5] = 31;
                    } else if (fields[5] > 31) {
                        fields[5] = 1;
                    }
                } else if (fields[2] > 6 && fields[2] <= 12) {
                    if (fields[2] == 12) {
                        if (!leapYear) {
                            if (fields[5] > 29) {
                                fields[5] = 1;
                            } else if (fields[5] < 1) {
                                fields[5] = 29;
                            }
                        } else {
                            if (fields[5] > 30) {
                                fields[5] = 1;
                            } else if (fields[5] < 1) {
                                fields[5] = 30;
                            }
                        }
                    } else {
                        if (fields[5] > 30) {
                            fields[5] = 1;
                        } else if (fields[5] < 1) {
                            fields[5] = 30;
                        }
                    }
                }
                break;
            case HOUR:
            case HOUR_OF_DAY:
                fields[11] += up ? +1 : -1;
                if (fields[11] > 23) {
                    fields[11] = 0;
                } else if (fields[11] < 0) {
                    fields[11] = 23;
                }
                break;
            case MINUTE:
                fields[12] += up ? +1 : -1;
                if (fields[12] == 60) {
                    fields[12] = 0;
                } else if (fields[12] == -1) {
                    fields[12] = 59;
                }
                break;
            case SECOND:
                fields[13] += up ? +1 : -1;
                if (fields[13] == 60) {
                    fields[13] = 0;
                } else if (fields[13] == -1) {
                    fields[13] = 59;
                }
                break;
            default:
                break;
        }
        fields[10] = fields[11];
    }

    /**
     * @return the time zone object associated with this calendar.
     */
    public TimeZone getTimeZone() {
        if (computeDesTimeZone) {
            return this.desTimeZone;
        } else {
            return super.getTimeZone();
        }
    }

    public boolean isCalcOverflow() {
        return calcOverflow;
    }

    public void setCalcOverflow(boolean calcOverflow) {
        this.calcOverflow = calcOverflow;
    }

    public int getFirstDayOfWeek() {
        return Calendar.SATURDAY;
    }
}
