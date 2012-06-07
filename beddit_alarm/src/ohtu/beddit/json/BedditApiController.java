package ohtu.beddit.json;

import android.content.Context;
import ohtu.beddit.web.BedditConnector;

import java.util.Calendar;

public class BedditApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;

    public BedditApiController(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    public int getUserCount(Context context){
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getUsercount();
    }

    public String getUsername(Context context, int userIndex){
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getUsername(userIndex);
    }

    public String getFirstName(Context context, int userIndex){
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getFirstName(userIndex);
    }

    public String getLastName(Context context, int userIndex){
        String json = bedditConnector.getUserJson(context);
        return jsonParser.getUsers(json).getLastName(userIndex);
    }

    public char getLastSleepStage(Context context){
        String json = bedditConnector.getWakeUpJson(context);
        return jsonParser.getNight(json).getLastSleepStage();
    }

    public Calendar getTimeOfLastSleepStage(Context context){
        String json = bedditConnector.getWakeUpJson(context);
        return jsonParser.getNight(json).getLastSleepStageTime();
    }
}

