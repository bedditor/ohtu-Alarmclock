package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.api.jsonclassimpl.QueueData;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.utils.TimeUtils;
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
        long atMostMillisOld = TimeUtils.MILLISECONDS_IN_MINUTE * AT_MOST_MINUTES_OLD;
        try {
            api.updateQueueData(context);
            QueueData queueData = api.getQueueData(context);
            Calendar now = Calendar.getInstance();
            Calendar updateUpToWhenAnalyzed = queueData.getWhenSleepAnalyzed();

            //<for tests>
            Calendar updateUpTo = queueData.getResults_available_up_to();
            long queueDifference;
            try {
                Calendar updateUpToWhenQueued = queueData.getWhen_queued_for_sleep_analysis();
                queueDifference = now.getTimeInMillis() - updateUpToWhenQueued.getTimeInMillis();
            } catch (NullPointerException n) {
                queueDifference = now.getTimeInMillis() - 999 * TimeUtils.MILLISECONDS_IN_MINUTE;
            }
            long updateDifference = now.getTimeInMillis() - updateUpTo.getTimeInMillis();
            //</for tests>

            long analysisDifference = now.getTimeInMillis() - updateUpToWhenAnalyzed.getTimeInMillis();
            Log.v(TAG, "Time difference from update? (minutes): " + updateDifference / TimeUtils.MILLISECONDS_IN_MINUTE);
            Log.v(TAG, "Time difference from analysis (minutes): " + analysisDifference / TimeUtils.MILLISECONDS_IN_MINUTE);
            Log.v(TAG, "Time difference from queued (minutes): " + queueDifference / TimeUtils.MILLISECONDS_IN_MINUTE);
            if (analysisDifference < atMostMillisOld || queueDifference < atMostMillisOld) {//should only use analysisDifference in final version
                api.updateSleepData(context);
                char lastSleepStage = api.getSleepData(context).getLastSleepStage();
                Log.v(TAG, "sleep stage: " + lastSleepStage);
                char[] sleepStages = getWakeUpSleepStages(context);
                for (char wakeUpSleepStage : sleepStages) {
                    if (lastSleepStage == wakeUpSleepStage) {
                        return true;
                    }
                }
            } else if (api.getQueueData(context).getSleepAnalysisStatus().equals("can_be_queued_for_analysis")) {
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