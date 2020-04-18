package veinthrough.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatUtils {
    private static final TimeZone SHANG_HAI = TimeZone.getTimeZone("Asia/Shanghai");
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS z";

    /**
     * Get date format by time zone and pattern
     * @param pattern pattern of the output string
     * @param timeZone timezone
     */
    public static java.text.DateFormat getDateFormat(String pattern, TimeZone timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern );
        format.setTimeZone(timeZone);
        return format;
    }

    /**
     * Get date format of SHANG_HAI time zone and DEFAULT_DATE_PATTERN
     */
    public static java.text.DateFormat getDateFormat() {
        return getDateFormat(DEFAULT_DATE_PATTERN, SHANG_HAI);
    }

    /**
     * Format date with date format by time zone and pattern
     * @param date time to format
     * @param pattern pattern of the output string
     * @param timeZone timezone
     */
    public static String formatDate(long date, TimeZone timeZone, String pattern) {
        return getDateFormat(pattern, timeZone).format(new Date(date));
    }

    /**
     * Format date with date format by SHANG_HAI time zone and DEFAULT_DATE_PATTERN
     * @param date time to format
     */
    public static String formatDate(long date) {
        return formatDate(date, SHANG_HAI, DEFAULT_DATE_PATTERN);
    }
}
