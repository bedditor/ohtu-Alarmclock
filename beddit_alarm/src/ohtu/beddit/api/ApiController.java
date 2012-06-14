package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.web.BedditConnectionException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Joza
 * Date: 14.6.2012
 * Time: 8:34
 * To change this template use File | Settings | File Templates.
 */
public interface ApiController {
    void updateUserInfo(Context context) throws BedditConnectionException;

    void updateSleepInfo(Context context) throws BedditConnectionException;

    void updateQueueInfo(Context context) throws BedditConnectionException;

    void requestInfoUpdate(Context context, String date) throws BedditConnectionException;

    String getUsername(Context context, int userIndex) throws BedditConnectionException;

    String getFirstName(Context context, int userIndex) throws BedditConnectionException;

    String getLastName(Context context, int userIndex) throws BedditConnectionException;

    char getLastSleepStage(Context context) throws BedditConnectionException;

    String getSleepAnalysisStatus(Context context) throws BedditConnectionException;

    Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditConnectionException;

    Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditConnectionException;

    Calendar getSleepAnalysisWhenQueued(Context context) throws BedditConnectionException;

    Calendar getTimeOfLastSleepStage(Context context) throws BedditConnectionException;

    int getTimeSleeping(Context context) throws BedditConnectionException;

    int getTimeDeepSleep(Context context) throws BedditConnectionException;

    String getLocalAnalyzedUpToTime(Context context) throws BedditConnectionException;

    boolean getIsAnalysisUpToDate(Context context) throws BedditConnectionException;
}
