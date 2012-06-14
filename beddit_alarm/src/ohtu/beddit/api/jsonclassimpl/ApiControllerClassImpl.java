package ohtu.beddit.api.jsonclassimpl;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.web.BedditConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApiControllerClassImpl implements ApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;
    private static String userjson = null;
    private static String sleepjson = null;
    private static String queuejson = null;

    public ApiControllerClassImpl(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    @Override
    public void updateUserInfo(Context context) throws MalformedBedditJsonException{
        userjson = bedditConnector.getUserJson(context);
        Log.v("apidapi", "update: "+userjson);
    }

    @Override
    public void updateSleepInfo(Context context, String date) throws MalformedBedditJsonException{
        sleepjson = bedditConnector.getWakeUpJson(context,date);
        Log.v("apidapi", "update: "+sleepjson);
    }

    @Override
    public void updateQueueInfo(Context context, String date) throws MalformedBedditJsonException{
        queuejson = bedditConnector.getQueueStateJson(context,date);
        Log.v("apidapi", "update: "+queuejson);
    }

    @Override
    public void requestInfoUpdate(Context context, String date) throws MalformedBedditJsonException{
        bedditConnector.requestDataAnalysis(context, date);
    }


    @Override
    public String getUsername(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getUsername(userIndex);
    }

    @Override
    public String getFirstName(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getFirstName(userIndex);
    }

    @Override
    public String getLastName(int userIndex) throws MalformedBedditJsonException {
        return jsonParser.getUsers(userjson).getLastName(userIndex);
    }

    @Override
    public char getLastSleepStage() throws MalformedBedditJsonException {
        return jsonParser.getNight(sleepjson).getLastSleepStage();
    }


    @Override
    public String getSleepAnalysisStatus() throws MalformedBedditJsonException {
        return jsonParser.getQueueData(queuejson).getSleepAnalysisStatus();
    }

    @Override
    public Calendar getTimeOfLastSleepStage() throws MalformedBedditJsonException {
        return jsonParser.getNight(sleepjson).getLastSleepStageTime();
    }
}

