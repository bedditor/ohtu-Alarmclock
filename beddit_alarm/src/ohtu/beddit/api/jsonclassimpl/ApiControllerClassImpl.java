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
    private static String userJson = null;
    private static String sleepJson = null;
    private static String queueJson = null;
    private static Calendar lastSleepUpdateTime = null;
    private static String lastUser = null;

    public ApiControllerClassImpl(){
        bedditConnector = new BedditWebConnector();
    }

    public ApiControllerClassImpl(BedditConnector bedditConnector) {
        this.bedditConnector = bedditConnector;
    }

    @Override
    public void updateUserData(Context context) throws BedditException {
        userJson = bedditConnector.getUserJson(context);
        Log.v("apidapi", "update: "+ userJson);
    }

    @Override
    public void updateSleepData(Context context) throws BedditException{
        String date = TimeUtils.getTodayAsQueryDateString();
        sleepJson = bedditConnector.getWakeUpJson(context,date);
        lastSleepUpdateTime = Calendar.getInstance();
        lastUser = PreferenceService.getUsername(context);
        Log.v("apidapi", "update: "+ sleepJson);
    }

    @Override
    public void updateQueueData(Context context) throws BedditException{
        String date = TimeUtils.getTodayAsQueryDateString();
        queueJson = bedditConnector.getQueueStateJson(context,date);
        Log.v("apidapi", "update: "+ queueJson);
    }

    @Override
    public boolean isSleepInfoOutdated(){
        if(lastSleepUpdateTime == null || TimeUtils.differenceInMinutes(Calendar.getInstance(), lastSleepUpdateTime) > 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasUserChanged(Context context){
        if(lastUser.equals(PreferenceService.getUsername(context))){
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void requestInfoUpdate(Context context) throws BedditException {
        Log.v("apidapi", "postattu: " + bedditConnector.requestDataAnalysis(context, TimeUtils.getTodayAsQueryDateString()));
    }


    @Override
    public String getUsername(Context context, int userIndex) throws BedditException {
        String json = getUserJson(context);
        return jsonParser.getUserData(json).getUsername(userIndex);
    }

    @Override
    public String getFirstName(Context context, int userIndex) throws BedditException {
        String json = getUserJson(context);
        return jsonParser.getUserData(json).getFirstName(userIndex);
    }

    @Override
    public String getLastName(Context context, int userIndex) throws BedditException {
        String json = getUserJson(context);
        return jsonParser.getUserData(json).getLastName(userIndex);
    }

    @Override
    public char getLastSleepStage(Context context) throws BedditException {
        String json = getSleepJson(context);
        return jsonParser.getSleepData(json).getLastSleepStage();
    }


    @Override
    public String getSleepAnalysisStatus(Context context) throws BedditException {
        String json = getQueueJson(context);
        return jsonParser.getQueueData(json).getSleepAnalysisStatus();
    }


    @Override
    public Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditException {
        String json = getQueueJson(context);
        return jsonParser.getQueueData(json).getResults_available_up_to();
    }

    @Override
    public Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditException {
        String json = getQueueJson(context);
        return jsonParser.getQueueData(json).getWhen_sleep_analyzed();
    }

    @Override
    public Calendar getSleepAnalysisWhenQueued(Context context) throws BedditException {
        String json = getQueueJson(context);
        return jsonParser.getQueueData(json).getWhen_queued_for_sleep_analysis();
    }


    @Override
    public int getTimeSleeping(Context context) throws BedditException {
        String json = getSleepJson(context);
        return jsonParser.getSleepData(json).getTimeSleeping();
    }

    @Override
    public int getTimeDeepSleep(Context context) throws BedditException {
        String json = getSleepJson(context);
        return jsonParser.getSleepData(json).getTimeDeepSleep();
    }

    @Override
    public String getLocalAnalyzedUpToTime(Context context) throws BedditException {
        String json = getSleepJson(context);
        return jsonParser.getSleepData(json).getLocal_analyzed_up_to_time();
    }

    @Override
    public boolean getIsAnalysisUpToDate(Context context) throws BedditException {
        String json = getSleepJson(context);
        return jsonParser.getSleepData(json).getIsAnalysisUpToDate();
    }


    private String getUserJson(Context context) throws BedditException {
        if(userJson ==null) updateUserData(context);
        return userJson;
    }

    private String getSleepJson(Context context) throws BedditException {
        if(sleepJson ==null) updateSleepData(context);
        return sleepJson;
    }

    private String getQueueJson(Context context) throws BedditException {
        if(queueJson ==null) updateQueueData(context);
        return queueJson;
    }




}

