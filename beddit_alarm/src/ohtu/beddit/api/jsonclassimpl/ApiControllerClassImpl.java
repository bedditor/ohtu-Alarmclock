package ohtu.beddit.api.jsonclassimpl;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.*;

import java.util.Calendar;

public class ApiControllerClassImpl implements ApiController {


    private static BedditJsonParser jsonParser = new BedditJsonParserImpl();
    private BedditConnector bedditConnector;
    private static String userjson = null;
    private static String sleepjson = null;
    private static String queuejson = null;
    private static Calendar lastSleepUpdateTime = null;
    private static String lastUser = null;

    public ApiControllerClassImpl(){
        bedditConnector = new BedditWebConnector();
    }

    public ApiControllerClassImpl(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    @Override
    public void updateUserInfo(Context context) throws BedditException {
        userjson = bedditConnector.getUserJson(context);
        Log.v("apidapi", "update: "+userjson);
    }

    @Override
    public void updateSleepInfo(Context context) throws BedditException{
        String date = TimeUtils.getTodayAsQueryDateString();
        sleepjson = bedditConnector.getWakeUpJson(context,date);
        lastSleepUpdateTime = Calendar.getInstance();
        lastUser = PreferenceService.getUsername(context);
        Log.v("apidapi", "update: "+sleepjson);
    }

    @Override
    public boolean sleepInfoOutdated(){
        if(lastSleepUpdateTime == null || TimeUtils.differenceInMinutes(Calendar.getInstance(), lastSleepUpdateTime) > 1){
            return true;
        }
        return false;
    }

    public boolean hasUserChanged(Context context){
        if(lastUser.equals(PreferenceService.getUsername(context))){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void updateQueueInfo(Context context) throws BedditException{
        String date = TimeUtils.getTodayAsQueryDateString();
        queuejson = bedditConnector.getQueueStateJson(context,date);
        Log.v("apidapi", "update: "+queuejson);
    }

    @Override
    public void requestInfoUpdate(Context context) throws BedditException {
        Log.v("apidapi", "postattu: " + bedditConnector.requestDataAnalysis(context, TimeUtils.getTodayAsQueryDateString()));
    }


    @Override
    public String getUsername(Context context, int userIndex) throws BedditException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getUsername(userIndex);
    }

    @Override
    public String getFirstName(Context context, int userIndex) throws BedditException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getFirstName(userIndex);
    }

    @Override
    public String getLastName(Context context, int userIndex) throws BedditException {
        String json = getUserjson(context);
        return jsonParser.getUsers(json).getLastName(userIndex);
    }

    @Override
    public char getLastSleepStage(Context context) throws BedditException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getLastSleepStage();
    }


    @Override
    public String getSleepAnalysisStatus(Context context) throws BedditException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getSleepAnalysisStatus();
    }


    @Override
    public Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getResults_available_up_to();
    }

    @Override
    public Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getWhen_sleep_analyzed();
    }

    @Override
    public Calendar getSleepAnalysisWhenQueued(Context context) throws BedditException {
        String json = getQueuejson(context);
        return jsonParser.getQueueData(json).getWhen_queued_for_sleep_analysis();
    }


    @Override
    public int getTimeSleeping(Context context) throws BedditException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getTimeSleeping();
    }

    @Override
    public int getTimeDeepSleep(Context context) throws BedditException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getTimeDeepSleep();
    }

    @Override
    public String getLocalAnalyzedUpToTime(Context context) throws BedditException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getLocal_analyzed_up_to_time();
    }

    @Override
    public boolean getIsAnalysisUpToDate(Context context) throws BedditException {
        String json = getSleepjson(context);
        return jsonParser.getNight(json).getIsAnalysisUpToDate();
    }


    private String getUserjson(Context context) throws BedditException {
        if(userjson==null) updateUserInfo(context);
        return userjson;
    }

    private String getSleepjson(Context context) throws BedditException {
        if(sleepjson==null) updateSleepInfo(context);
        return sleepjson;
    }

    private String getQueuejson(Context context) throws BedditException {
        if(queuejson==null) updateQueueInfo(context);
        return queuejson;
    }




}

