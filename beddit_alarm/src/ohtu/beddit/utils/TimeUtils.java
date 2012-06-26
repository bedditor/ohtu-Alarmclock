package ohtu.beddit.utils;

import android.content.Context;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for different time operations, i.e. displaying time in correct form or making
 * a correct String for queries.
 */
public class TimeUtils {

    public static final int MILLISECONDS_IN_MINUTE = 60000;

    /**
     * Converts time in beddit format to {@link Calendar} object
     */
    public static Calendar bedditTimeStringToCalendar(String timeString) {
        timeString = timeString.replaceAll("T", " ");
        Date date;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("Night: " + e.getMessage());
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Return a string representing current date which is needed in api query url.
     */
    public static String getTodayAsQueryDateString() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + (month + 1) + "/" + day; // January is 0 in Calendar, but 1 in query
    }

    /**
     * Return localized string presentation of the given time.
     * Needs some android context to read date format from system.
     */
    public static String timeAsString(int hour, int minute, Context context) {
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);

        SimpleDateFormat dateFormat;
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            dateFormat = new SimpleDateFormat("HH:mm");
        } else {
            dateFormat = new SimpleDateFormat("h:mm a");
        }

        return dateFormat.format(date);
    }

    public static long differenceInMinutes(Calendar a, Calendar b) {
        return (Math.abs(a.getTimeInMillis() - b.getTimeInMillis())) / MILLISECONDS_IN_MINUTE;
    }

    /**
     * Creates a Calendar object representing the next time with given hours and minutes and zero seconds.
     */
    public static Calendar timeToCalendar(int hours, int minutes) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hours);
        time.set(Calendar.MINUTE, minutes);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        Calendar currentTime = Calendar.getInstance();
        if (time.before(currentTime)) {
            time.add(Calendar.DAY_OF_YEAR, 1);
        }
        return time;
    }


    /**
     * Compares device time to beddit time and returns the time difference as human readable String.
     *
     * @param data Expects date format YYYY-MM-DDThh:mm:ss
     * @return Returns the time difference. Uses getHoursAndMinutesFromSeconds return output for format.
     */
    public static String getTimeDifference(String data) {
        if (data == null) return "--";
        Calendar time = bedditTimeStringToCalendar(data);
        int differenceInSeconds = (int) ((Calendar.getInstance().getTimeInMillis() - time.getTimeInMillis()) / 1000);
        return getHoursAndMinutesFromSeconds(differenceInSeconds);
    }


    /**
     * Method will return viewable String that describes seconds as hours and minutes.
     *
     * @param seconds Expects seconds to be given which will then be split to hours and minutes.
     * @return Will return String in the format of h + "h " + mm + "min". Ex. "6h 30min" or "10h 2min".
     */
    public static String getHoursAndMinutesFromSeconds(int seconds) {
        return seconds / 3600 + "h " + (seconds / 60) % 60 + "min";
    }
}
