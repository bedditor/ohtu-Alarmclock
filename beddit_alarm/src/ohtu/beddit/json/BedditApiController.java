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
    private static String userjson = null;
    private static String sleepjson = null;
    private static String queuejson = null;

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

    public void updateUserInfo(Context context) throws MalformedBedditJsonException{
        userjson = bedditConnector.getUserJson(context);
        Log.v("apidapi", "update: "+userjson);
    }

    public void updateSleepInfo(Context context, String date) throws MalformedBedditJsonException{
        sleepjson = bedditConnector.getWakeUpJson(context,date);
        Log.v("apidapi", "update: "+sleepjson);
    }

    public void updateQueueInfo(Context context, String date) throws MalformedBedditJsonException{
        queuejson = bedditConnector.getQueueStateJson(context,date);
        Log.v("apidapi", "update: "+queuejson);
    }

    public void requestInfoUpdate(Context context, String date) throws MalformedBedditJsonException{
        bedditConnector.requestDataAnalysis(context, date);
    }


    public String getUsername(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getUsername(userIndex);
    }

    public String getFirstName(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getFirstName(userIndex);
    }

    public String getLastName(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getLastName(userIndex);
    }

    public char getLastSleepStage() throws MalformedBedditJsonException {
        return jsonParser.getNight(sleepjson).getLastSleepStage();
    }


    public String getSleepAnalysisStatus() throws MalformedBedditJsonException {
        return jsonParser.getQueueData(queuejson).getSleepAnalysisStatus();
    }

    public Calendar getTimeOfLastSleepStage() throws MalformedBedditJsonException {
        return jsonParser.getNight(sleepjson).getLastSleepStageTime();
    }
}

