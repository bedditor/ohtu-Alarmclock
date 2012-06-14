package ohtu.beddit.utils;

import android.content.Context;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static Calendar bedditTimeStringToCalendar(String timeString){
        timeString = timeString.replaceAll("T", " ");
        Date date;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = (Date)dateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("Night: "+e.getMessage());
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String getTodayAsQueryDateString(){
        Calendar kalenteri = Calendar.getInstance();
        int year = kalenteri.get(Calendar.YEAR);
        int month = kalenteri.get(Calendar.MONTH);
        int day = kalenteri.get(Calendar.DAY_OF_MONTH);
        return year+"/"+(month+1)+"/"+day; //because the fucking months start from 0
    }


    public static String timeAsString(int hour, int minute, Context context){
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(minute);

        SimpleDateFormat dateFormat;
        if(android.text.format.DateFormat.is24HourFormat(context)){
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        else{
            dateFormat = new SimpleDateFormat("h:mm a");
        }

        return dateFormat.format(date);
    }

    public static boolean isDifferenceGreaterThanXMinutes(Calendar a, Calendar b, int minutes){
        long timeA = a.getTimeInMillis();
        long timeB = b.getTimeInMillis();
        long difference = (Math.abs(timeA - timeB))/1000/60;
        if(difference > minutes){
            return true;
        }else{
            return false;
        }

    }


}
