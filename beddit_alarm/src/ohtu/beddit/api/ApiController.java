package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.api.jsonclassimpl.QueueData;
import ohtu.beddit.api.jsonclassimpl.SleepData;
import ohtu.beddit.api.jsonclassimpl.UserData;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditException;

public interface ApiController {

    /**
     * Retrieves user data json from Beddit with {@link ohtu.beddit.web.BedditWebConnector}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateUserData(Context context) throws BedditConnectionException;

    /**
     * Retrieves sleep data json from Beddit with {@link ohtu.beddit.web.BedditWebConnector}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateSleepData(Context context) throws BedditConnectionException;

    /**
     * Retrieves queue data json from Beddit with {@link ohtu.beddit.web.BedditWebConnector}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateQueueData(Context context) throws BedditConnectionException;

    void requestInfoUpdate(Context context) throws BedditConnectionException;

    String getAccessToken(Context context, String url) throws BedditException;

    /**
     * Parses saved queue data json and returns it as {@link QueueData} object,
     * Remember to call {@link #updateQueueData(android.content.Context)} first if needed.
     * @param context an
     * @return data about api queue status
     * @throws BedditException
     */
    QueueData getQueueData(Context context) throws BedditException;

    UserData getUserData(Context context) throws BedditException;

    SleepData getSleepData(Context context) throws BedditException;

    boolean hasUserChanged(Context context);

    boolean isSleepInfoOutdated();

}
