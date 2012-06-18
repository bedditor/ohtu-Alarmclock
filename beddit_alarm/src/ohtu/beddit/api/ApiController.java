package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.api.jsonclassimpl.InvalidJsonException;
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

    void requestInfoUpdate(Context context) throws BedditConnectionException;

    String getUsername(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException;

    String getFirstName(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException;

    String getLastName(Context context, int userIndex) throws BedditConnectionException, InvalidJsonException;

    char getLastSleepStage(Context context) throws BedditConnectionException, InvalidJsonException;

    String getSleepAnalysisStatus(Context context) throws BedditConnectionException, InvalidJsonException;

    Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditConnectionException, InvalidJsonException;

    Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditConnectionException, InvalidJsonException;

    Calendar getSleepAnalysisWhenQueued(Context context) throws BedditConnectionException, InvalidJsonException;

    int getTimeSleeping(Context context) throws BedditConnectionException, InvalidJsonException;

    int getTimeDeepSleep(Context context) throws BedditConnectionException, InvalidJsonException;


    String getLocalAnalyzedUpToTime(Context context) throws BedditConnectionException, InvalidJsonException;

    boolean isSleepInfoFuckingOld();


    boolean getIsAnalysisUpToDate(Context context) throws BedditConnectionException, InvalidJsonException;
}
