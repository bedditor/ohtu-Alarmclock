package ohtu.beddit.json;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.web.BedditConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.util.Calendar;

public class BedditApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;

    public BedditApiController(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
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

    public char getLastSleepStage(Context context) throws MalformedBedditJsonException {
        String json = bedditConnector.getWakeUpJson(context);
        return jsonParser.getNight(json).getLastSleepStage();
    }

    public Calendar getTimeOfLastSleepStage(Context context) throws MalformedBedditJsonException {
        String json = bedditConnector.getWakeUpJson(context);
        return jsonParser.getNight(json).getLastSleepStageTime();
    }
}

