package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Joza
 * Date: 14.6.2012
 * Time: 8:34
 * To change this template use File | Settings | File Templates.
 */
public interface ApiController {
    void updateUserInfo(Context context) throws MalformedBedditJsonException;

    void updateSleepInfo(Context context, String date) throws MalformedBedditJsonException;

    void updateQueueInfo(Context context, String date) throws MalformedBedditJsonException;

    void requestInfoUpdate(Context context, String date) throws MalformedBedditJsonException;

    String getUsername(int userIndex) throws MalformedBedditJsonException;

    String getFirstName(int userIndex) throws MalformedBedditJsonException;

    String getLastName(int userIndex) throws MalformedBedditJsonException;

    char getLastSleepStage() throws MalformedBedditJsonException;

    String getSleepAnalysisStatus() throws MalformedBedditJsonException;

    Calendar getTimeOfLastSleepStage() throws MalformedBedditJsonException;

    Calendar getSleepAnalysisResultsUpTo() throws MalformedBedditJsonException;

    Calendar getSleepAnalysisWhenAnalyzed() throws MalformedBedditJsonException;

    Calendar getSleepAnalysisWhenQueued() throws  MalformedBedditJsonException;
}
