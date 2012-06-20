package ohtu.beddit.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

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

    public static String getTodayAsQueryDateString() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "/" + (month + 1) + "/" + day; //because the fucking months start from 0
    }


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
        long timeA = a.getTimeInMillis();
        long timeB = b.getTimeInMillis();
        return (Math.abs(timeA - timeB)) / 1000 / 60;
    }

    public static Calendar timeToCalendar(int hours, int minutes) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hours);
        time.set(Calendar.MINUTE, minutes);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        Calendar currentTime = Calendar.getInstance();
        if (time.before(currentTime)) time.add(Calendar.DAY_OF_YEAR, 1);
        return time;
    }


}
