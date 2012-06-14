package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 5.6.2012
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class AlarmCheckerRealImpl implements AlarmChecker{

    @Override
    public boolean wakeUpNow(Context context,char sleepstage) {

        ApiController api = new ApiControllerClassImpl(new BedditWebConnector());
        int minutes = 2;
        long atMostMillisOld = 1000 * 60 * minutes;
        String dateString = getQueryDateString();
        try{
            api.updateQueueInfo(context, dateString);
            Calendar nao = Calendar.getInstance();
            //
            Calendar updateUpToWhenAnalyzed = api.getSleepAnalysisWhenAnalyzed();
            Calendar updateUpTo = api.getSleepAnalysisResultsUpTo();
            long queuedifference = 0;
            try{
                Calendar updateUpToWhenQueued = api.getSleepAnalysisWhenQueued();
                queuedifference = nao.getTimeInMillis() - updateUpToWhenQueued.getTimeInMillis();
            }catch(NullPointerException n){
                queuedifference = nao.getTimeInMillis() - 999*60*1000;
            }
            //Calendar.getTimeInMillis() am / pm problem?
            long updatedifference = nao.getTimeInMillis() - updateUpTo.getTimeInMillis();
            long analysisdifference = nao.getTimeInMillis() - updateUpToWhenAnalyzed.getTimeInMillis();
            Log.v("apidapi", "Time difference from update? (minutes): "+updatedifference/60/1000);
            Log.v("apidapi", "Time difference from analysis (minutes): "+analysisdifference/60/1000);
            Log.v("apidapi", "Time difference from queued (minutes): "+queuedifference/60/1000);
            if(analysisdifference < atMostMillisOld){
                api.updateSleepInfo(context,dateString);
                Log.v("apidapi", "sleepstage: "+api.getLastSleepStage());
                if(api.getLastSleepStage() == sleepstage){
                    return true;
                }else{
                    return false;
                }
            }
            if(api.getSleepAnalysisStatus().equals("can_be_queued_for_analysis")){
                Log.v("apidapi", "update request");
                api.requestInfoUpdate(context,dateString);
            }else{
                Log.v("apidapi", "was ist das");
            }
            return false;
        }catch(MalformedBedditJsonException e){
            Log.v("apidapi", Log.getStackTraceString(e));
            Log.v("apidapi", "fug");
            return false;
        }
    }

    public static String getQueryDateString(){
        Calendar kalenteri = Calendar.getInstance();
        int year = kalenteri.get(Calendar.YEAR);
        int month = kalenteri.get(Calendar.MONTH);
        int day = kalenteri.get(Calendar.DAY_OF_MONTH);
        return year+"/"+(month+1)+"/"+day; //because the fucking months start from 0
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 180;
    }

    @Override
    public int getCheckTime() {
        return 5;
    }
}