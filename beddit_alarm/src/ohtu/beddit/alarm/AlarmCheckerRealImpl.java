package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonparser.classimpl.ApiControllerClassImpl;
import ohtu.beddit.api.jsonparser.classimpl.QueueData;
import ohtu.beddit.io.PreferenceService;
import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.BedditException;

import java.util.Calendar;
/**
 * This class is used to check whether or not the alarm should go off
 * at any given moment based on the sleep stages selected.
 */
public class AlarmCheckerRealImpl implements AlarmChecker {

    private static final String TAG = "AlarmChecker";
    private static final char BOTH_SLEEP_STAGES = 'b';
    private static final char AWAKE = 'W';
    private static final char AWAY = 'A';
    private static final char REM_SLEEP_STAGE = 'R';
    private static final char LIGHT_SLEEP_STAGE = 'L';
    private static final int AT_MOST_MINUTES_OLD = 5;

    /**
     * Tries to update data. After that it will check wether it's time to wake up.
     * @param context AlarmChecker needs application context for possible PreferenceService or API calls
     * @return Returns a boolean depending is it time to wake up or not.
     */
    @Override
    public boolean wakeUpNow(Context context) {

        ApiController api = new ApiControllerClassImpl();
        long atMostMillisOld = TimeUtils.MILLISECONDS_IN_MINUTE * AT_MOST_MINUTES_OLD;
        try {
            api.updateQueueData(context);
            QueueData queueData = api.getQueueData(context);
            Calendar now = Calendar.getInstance();
            Calendar updateUpToWhenAnalyzed = queueData.getWhenSleepAnalyzed();

            long analysisDifference = now.getTimeInMillis() - updateUpToWhenAnalyzed.getTimeInMillis();

            if (analysisDifference < atMostMillisOld) {
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

    /**
     * Handles which sleep stage to wake up from. Gets the data from PreferenceService.
     * @param context
     * @return an array of characters which represent the possible sleep stages the user should be woken up from.
     */
    private char[] getWakeUpSleepStages(Context context) {
        char sleepStage = PreferenceService.getWakeUpSleepStage(context);
        char[] sleepStages = {REM_SLEEP_STAGE, LIGHT_SLEEP_STAGE, AWAKE, AWAY};
        if (sleepStage != BOTH_SLEEP_STAGES) {
            sleepStages = new char[3];
            sleepStages[0] = sleepStage;
            sleepStages[1] = AWAKE;
            sleepStages[2] = AWAY;
        }
        return sleepStages;
    }

    /**
     * Return time interval.
     * @return returns 3 minutes.
     */
    @Override
    public int getWakeUpAttemptInterval() {
        return 3*60;
    }

}