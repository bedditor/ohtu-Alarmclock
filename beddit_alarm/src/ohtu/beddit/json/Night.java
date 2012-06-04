package ohtu.beddit.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 31.5.2012
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class Night extends JsonObject{
    private String date;
    private String[][] sleep_stages;

    public String getDate() {
        return date;
    }

    public char getLastSleepStage(){
        return sleep_stages[sleep_stages.length-1][1].charAt(0);
    }

    public Calendar getLastSleepStageTime(){
        String timeString = sleep_stages[sleep_stages.length-1][0];
        return bedditTimeStringToCalendar(timeString);
    }

    private Calendar bedditTimeStringToCalendar(String timeString){
        timeString = timeString.replaceAll("T", " ");
        Date date;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date = (Date)dateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("Night: "+e.getMessage());
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }



}
