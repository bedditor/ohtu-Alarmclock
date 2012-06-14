package ohtu.beddit.api.jsonclassimpl;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.utils.Utils;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditConnector;
import ohtu.beddit.web.BedditWebConnector;

import java.util.Calendar;

public class ApiControllerClassImpl implements ApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;
    private static String userjson = null;
    private static String sleepjson = null;
    private static String queuejson = null;

    public ApiControllerClassImpl(){
        bedditConnector = new BedditWebConnector();
    }

    public ApiControllerClassImpl(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    public void updateUserInfo(Context context) throws BedditConnectionException {
        userjson = bedditConnector.getUserJson(context);
        Log.v("apidapi", "update: "+userjson);
    }

    @Override
    public void updateSleepInfo(Context context) throws BedditConnectionException {
        String date = Utils.getTodayAsQueryDateString();
        sleepjson = bedditConnector.getWakeUpJson(context,date);
        Log.v("apidapi", "update: "+sleepjson);
    }

    @Override
    public void updateQueueInfo(Context context) throws BedditConnectionException {
        String date = Utils.getTodayAsQueryDateString();
        queuejson = bedditConnector.getQueueStateJson(context,date);
        Log.v("apidapi", "update: "+queuejson);
    }

    @Override
    public void requestInfoUpdate(Context context, String date) throws BedditConnectionException {
        Log.v("apidapi", "postattu: " + bedditConnector.requestDataAnalysis(context, date));
    }


    @Override
    public String getUsername(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getUsername(userIndex);
    }

    @Override
    public String getFirstName(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getFirstName(userIndex);
    }

    @Override
    public String getLastName(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getLastName(userIndex);
    }

    @Override
    public char getLastSleepStage(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getLastSleepStage();
    }


    @Override
    public String getSleepAnalysisStatus(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getSleepAnalysisStatus();
    }


    @Override
    public Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getResults_available_up_to();
    }

    @Override
    public Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getWhen_sleep_analyzed();
    }

    @Override
    public Calendar getSleepAnalysisWhenQueued(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getWhen_queued_for_sleep_analysis();
    }

    @Override
    public Calendar getTimeOfLastSleepStage(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getLastSleepStageTime();
    }

    @Override
    public int getTimeSleeping(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getTimeSleeping();
    }

    @Override
    public int getTimeDeepSleep(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getTimeDeepSleep();
    }

    @Override
    public String getLocalAnalyzedUpToTime(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getLocal_analyzed_up_to_time();
    }

    @Override
    public boolean getIsAnalysisUpToDate(Context context) throws BedditConnectionException, InvalidJsonException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getIsAnalysisUpToDate();
    }



    private String getUserjson(Context context) throws BedditConnectionException {
        if(userjson==null) updateUserInfo(context);
        return userjson;
    }

    private String getSleepjson(Context context) throws BedditConnectionException {
        if(sleepjson==null) updateSleepInfo(context);
        return sleepjson;
    }

    private String getQueuejson(Context context) throws BedditConnectionException {
        if(queuejson==null) updateQueueInfo(context);
        return queuejson;
    }



}

