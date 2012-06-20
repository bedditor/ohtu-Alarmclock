package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.BedditException;

import java.util.Calendar;

public class AlarmCheckerRealImpl implements AlarmChecker{

    private static final String TAG = "AlarmChecker";
    private static final char BOTH_SLEEP_STAGES = 'b';
    private static final char AWAKE = 'W';
    private static final char AWAY = 'A';
    private static final char REM_SLEEP_STAGE = 'R';
    private static final char LIGHT_SLEEP_STAGE = 'L';
    private static final int AT_MOST_MINUTES_OLD = 2;

    private static final char DIS_BE_TESTING = 'M';

    @Override
    public boolean wakeUpNow(Context context) {

        ApiController api = new ApiControllerClassImpl();
        long atMostMillisOld = 1000 * 60 * AT_MOST_MINUTES_OLD;
        try{
            api.updateQueueData(context);
            Calendar nao = Calendar.getInstance();
            Calendar updateUpToWhenAnalyzed = api.getSleepAnalysisWhenAnalyzed(context);

        //<for tests>
            Calendar updateUpTo = api.getSleepAnalysisResultsUpTo(context);
            long queuedifference = 0;
            try{
                Calendar updateUpToWhenQueued = api.getSleepAnalysisWhenQueued(context);
                queuedifference = nao.getTimeInMillis() - updateUpToWhenQueued.getTimeInMillis();
            }catch(NullPointerException n){
                queuedifference = nao.getTimeInMillis() - 999*60*1000;
            }
            long updatedifference = nao.getTimeInMillis() - updateUpTo.getTimeInMillis();
        //</for tests>
            long analysisdifference = nao.getTimeInMillis() - updateUpToWhenAnalyzed.getTimeInMillis();
            Log.v("apidapi", "Time difference from update? (minutes): "+updatedifference/60/1000);
            Log.v("apidapi", "Time difference from analysis (minutes): "+analysisdifference/60/1000);
            Log.v("apidapi", "Time difference from queued (minutes): "+queuedifference/60/1000);
            if(analysisdifference < atMostMillisOld || queuedifference < atMostMillisOld){//should only use analysisdifference in final version
                api.updateSleepData(context);
                Log.v("apidapi", "sleepstage: "+api.getLastSleepStage(context));
                char[] sleepStages = getWakeUpSleepStages(context);
                for (int i = 0; i< sleepStages.length; i++){
                    if(api.getLastSleepStage(context) == sleepStages[i]){
                        return true;
                    }
                }
            }else if(api.getSleepAnalysisStatus(context).equals("can_be_queued_for_analysis")){
                Log.v("apidapi", "update request");
                api.requestInfoUpdate(context);
            }else{
                Log.v("apidapi", "was ist das");
            }
            return false;
        }
        catch(BedditException e){
            Log.v(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    private char[] getWakeUpSleepStages(Context context){
        char sleepStage = PreferenceService.getWakeUpSleepStage(context);
        char[] sleepStages = {REM_SLEEP_STAGE, LIGHT_SLEEP_STAGE, AWAKE, AWAY, DIS_BE_TESTING};
        if(sleepStage != BOTH_SLEEP_STAGES){
            sleepStages = new char[3];
            sleepStages[0] = sleepStage;
            sleepStages[1] = AWAKE;
            sleepStages[2] = AWAY;
        }
        return sleepStages;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 30;
    }

    @Override
    public int getCheckTime() {
        return 5;
    }
}