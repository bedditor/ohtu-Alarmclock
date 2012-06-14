package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.api.jsonclassimpl.InvalidJsonException;
import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.BedditConnectionException;

import java.util.Calendar;

public class AlarmCheckerRealImpl implements AlarmChecker{

    private static final String TAG = "AlarmChecker";

    @Override
    public boolean wakeUpNow(Context context,char sleepstage) {

        ApiController api = new ApiControllerClassImpl();
        int minutes = 2;
        long atMostMillisOld = 1000 * 60 * minutes;
        String dateString = TimeUtils.getTodayAsQueryDateString();

        try{
            api.updateQueueInfo(context);
            Calendar nao = Calendar.getInstance();
            //
            Calendar updateUpToWhenAnalyzed = api.getSleepAnalysisWhenAnalyzed(context);
            Calendar updateUpTo = api.getSleepAnalysisResultsUpTo(context);
            long queuedifference = 0;
            try{
                Calendar updateUpToWhenQueued = api.getSleepAnalysisWhenQueued(context);
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
            if(analysisdifference < atMostMillisOld || queuedifference < atMostMillisOld){
                api.updateSleepInfo(context);
                Log.v("apidapi", "sleepstage: "+api.getLastSleepStage(context));
                if(api.getLastSleepStage(context) == sleepstage){
                    return true;
                }else{
                    return false;
                }
            }
            if(api.getSleepAnalysisStatus(context).equals("can_be_queued_for_analysis")){
                Log.v("apidapi", "update request");
                api.requestInfoUpdate(context,dateString);
            }else{
                Log.v("apidapi", "was ist das");
            }
            return false;
        }
        catch(BedditConnectionException e){
            Log.v(TAG, Log.getStackTraceString(e));
            return false;
        }
        catch(InvalidJsonException e){
            Log.v(TAG, Log.getStackTraceString(e));
            return false;
        }
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