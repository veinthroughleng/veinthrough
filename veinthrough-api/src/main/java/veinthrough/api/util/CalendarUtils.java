package veinthrough.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CalendarUtils {
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * get calendar of the time
     * @param time the time to get calendar from
     */
    public static Calendar getCalendar(long time) {
        Calendar calendar= GregorianCalendar.getInstance();
        calendar.setTime(new Date(time));
        return calendar;
    }

    /**
     * get calendar of now
     */
    public static Calendar getCalendar() {
        return getCalendar(now());
    }

    /**
     * next calendar of the time by field
     * @param time the time to get calendar from
     * @param field {@link Calendar#DAY_OF_MONTH}{@link Calendar#MONTH}{@link Calendar#YEAR} ...
     */
    public static Calendar nextCalendar(long time, int field) {
        return getCalendar(nextTime(time, field));
    }

    /**
     * next calendar of now by field
     * @param field {@link Calendar#DAY_OF_MONTH}{@link Calendar#MONTH}{@link Calendar#YEAR} ...
     */
    public static Calendar nextCalendar(int field) {
        return nextCalendar(now(), field);
    }

    /**
     * next time of the time by field
     * @param time the time to get calendar from
     * @param field {@link Calendar#DAY_OF_MONTH}{@link Calendar#MONTH}{@link Calendar#YEAR} ...
     */
    public static long nextTime(long time, int field) {
        Calendar calendar = getCalendar(time);
        calendar.add(field, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * next time of the now by field
     * @param field {@link Calendar#DAY_OF_MONTH}{@link Calendar#MONTH}{@link Calendar#YEAR} ...
     */
    public static long nextTime(int field) {
        return nextTime(now(), field);
    }
}
