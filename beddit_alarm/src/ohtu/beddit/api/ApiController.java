package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.api.jsonclassimpl.InvalidJsonException;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.UnauthorizedException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Joza
 * Date: 14.6.2012
 * Time: 8:34
 * To change this template use File | Settings | File Templates.
 */
public interface ApiController {
    void updateUserData(Context context) throws BedditException;

    void updateSleepData(Context context) throws BedditException;

    void updateQueueData(Context context) throws BedditException;

    void requestInfoUpdate(Context context) throws BedditException;

    String getUsername(Context context, int userIndex) throws BedditException;

    String getFirstName(Context context, int userIndex) throws BedditException;

    String getLastName(Context context, int userIndex) throws BedditException;

    char getLastSleepStage(Context context) throws BedditException;

    String getSleepAnalysisStatus(Context context) throws BedditException;

    Calendar getSleepAnalysisResultsUpTo(Context context) throws BedditException;

    Calendar getSleepAnalysisWhenAnalyzed(Context context) throws BedditException;

    Calendar getSleepAnalysisWhenQueued(Context context) throws BedditException;

    int getTimeSleeping(Context context) throws BedditException;

    int getTimeDeepSleep(Context context) throws BedditException;

    boolean hasUserChanged(Context context);


    String getLocalAnalyzedUpToTime(Context context) throws BedditException;

    boolean isSleepInfoOutdated();


    boolean getIsAnalysisUpToDate(Context context) throws BedditException;
}
