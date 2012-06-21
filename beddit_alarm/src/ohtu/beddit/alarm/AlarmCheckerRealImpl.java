package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.web.BedditException;

import java.util.Calendar;
/*
This class is used to check whether or not the alarm should go off at any given moment based on the sleep stages selected.
 */
public class AlarmCheckerRealImpl implements AlarmChecker {

    private static final String TAG = "AlarmChecker";
    private static final char BOTH_SLEEP_STAGES = 'b';
    private static final char AWAKE = 'W';
    private static final char AWAY = 'A';
    private static final char REM_SLEEP_STAGE = 'R';
    private static final char LIGHT_SLEEP_STAGE = 'L';
    private static final int AT_MOST_MINUTES_OLD = 5;

    private static final char DIS_BE_TESTING = 'M';

    @Override
    public boolean wakeUpNow(Context context) {

        ApiController api = new ApiControllerClassImpl();
        long atMostMillisOld = 1000 * 60 * AT_MOST_MINUTES_OLD;
        try {
            api.updateQueueData(context);
            Calendar now = Calendar.getInstance();
            Calendar updateUpToWhenAnalyzed = api.getSleepAnalysisWhenAnalyzed(context);

            //<for tests>
            Calendar updateUpTo = api.getSleepAnalysisResultsUpTo(context);
            long queueDifference;
            try {
                Calendar updateUpToWhenQueued = api.getSleepAnalysisWhenQueued(context);
                queueDifference = now.getTimeInMillis() - updateUpToWhenQueued.getTimeInMillis();
            } catch (NullPointerException n) {
                queueDifference = now.getTimeInMillis() - 999 * 60 * 1000;
            }
            long updateDifference = now.getTimeInMillis() - updateUpTo.getTimeInMillis();
            //</for tests>
            long analysisDifference = now.getTimeInMillis() - updateUpToWhenAnalyzed.getTimeInMillis();
            Log.v(TAG, "Time difference from update? (minutes): " + updateDifference / 60 / 1000);
            Log.v(TAG, "Time difference from analysis (minutes): " + analysisDifference / 60 / 1000);
            Log.v(TAG, "Time difference from queued (minutes): " + queueDifference / 60 / 1000);
            if (analysisDifference < atMostMillisOld || queueDifference < atMostMillisOld) {//should only use analysisDifference in final version
                api.updateSleepData(context);
                Log.v(TAG, "sleep stage: " + api.getLastSleepStage(context));
                char[] sleepStages = getWakeUpSleepStages(context);
                for (char sleepStage : sleepStages) {
                    if (api.getLastSleepStage(context) == sleepStage) {
                        return true;
                    }
                }
            } else if (api.getSleepAnalysisStatus(context).equals("can_be_queued_for_analysis")) {
                Log.v(TAG, "update request");
                api.requestInfoUpdate(context);
            } else {
                Log.v(TAG, "was ist das");
            }
            return false;
        } catch (BedditException e) {
            Log.v(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    private char[] getWakeUpSleepStages(Context context) {
        char sleepStage = PreferenceService.getWakeUpSleepStage(context);
        char[] sleepStages = {REM_SLEEP_STAGE, LIGHT_SLEEP_STAGE, AWAKE, AWAY, DIS_BE_TESTING};
        if (sleepStage != BOTH_SLEEP_STAGES) {
            sleepStages = new char[3];
            sleepStages[0] = sleepStage;
            sleepStages[1] = AWAKE;
            sleepStages[2] = AWAY;
        }
        return sleepStages;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 20;
    }

    @Override
    public int getCheckTime() {
        return 5;
    }
}