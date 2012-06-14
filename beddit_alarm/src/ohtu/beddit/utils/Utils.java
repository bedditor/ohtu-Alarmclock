package ohtu.beddit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Joza
 * Date: 14.6.2012
 * Time: 8:52
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

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
}
