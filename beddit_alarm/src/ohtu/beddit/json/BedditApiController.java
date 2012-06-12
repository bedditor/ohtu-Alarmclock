package ohtu.beddit.json;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.web.BedditConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BedditApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;

    public BedditApiController(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    public static Calendar bedditTimeStringToCalendar(String timeString){
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

    public String getUsername(Context context, int userIndex) throws MalformedBedditJsonException {
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getUsername(userIndex);
    }

    public String getFirstName(Context context, int userIndex) throws MalformedBedditJsonException {
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getFirstName(userIndex);
    }

    public String getLastName(Context context, int userIndex) throws MalformedBedditJsonException {
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getLastName(userIndex);
    }

    public char getLastSleepStage(Context context, String date) throws MalformedBedditJsonException {
        String json = bedditConnector.getWakeUpJson(context, date);
        return jsonParser.getNight(json).getLastSleepStage();
    }

    public Calendar getTimeOfLastSleepStage(Context context, String date) throws MalformedBedditJsonException {
        String json = bedditConnector.getWakeUpJson(context, date);
        return jsonParser.getNight(json).getLastSleepStageTime();
    }
}

