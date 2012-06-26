package ohtu.beddit.api;

import android.content.Context;
import ohtu.beddit.api.jsonparser.classimpl.QueueData;
import ohtu.beddit.api.jsonparser.classimpl.SleepData;
import ohtu.beddit.api.jsonparser.classimpl.UserData;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditException;

/**
 * Interface for requesting data updates and getting the data from Beddit API.
 */
public interface ApiController {

    /**
     * Retrieves user data json from Beddit with {@link ohtu.beddit.web.BedditConnectorImpl#getJsonFromServer(String, boolean)}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateUserData(Context context) throws BedditConnectionException;

    /**
     * Retrieves sleep data json from Beddit with {@link ohtu.beddit.web.BedditConnectorImpl#getJsonFromServer(String, boolean)}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateSleepData(Context context) throws BedditConnectionException;

    /**
     * Retrieves queue data json from Beddit with {@link ohtu.beddit.web.BedditConnectorImpl#getJsonFromServer(String, boolean)}
     * and saves it to a static variable.
     * @param context android context
     * @throws BedditConnectionException
     */
    void updateQueueData(Context context) throws BedditConnectionException;

    void requestInfoUpdate(Context context) throws BedditConnectionException;

    /**
     * Retrieves access token json from given url with {@link ohtu.beddit.web.BedditConnectorImpl#getJsonFromServer(String, boolean)},
     * parses it to {@link ohtu.beddit.api.jsonparser.classimpl.TokenData} with {@link ohtu.beddit.api.jsonparser.BedditJsonParser} and
     * returns access token as String
     *
     * @param url
     * @return access token as String
     * @throws BedditException
     */
    String getAccessToken(String url) throws BedditException;

    /**
     * Parses saved queue data json and returns it as {@link QueueData} object.
     * Remember to call {@link #updateQueueData(android.content.Context)} first if needed.
     * @param context android context
     * @return data about api queue status
     * @throws BedditException
     */
    QueueData getQueueData(Context context) throws BedditException;

    /**
     * Parses saved user data json and returns it as {@link UserData} object.
     * Remember to call {@link #updateUserData(android.content.Context)} first if needed.
     * @param context android context
     * @return data about users
     * @throws BedditException
     */
    UserData getUserData(Context context) throws BedditException;

    /**
     * Parses saved sleep data json and returns it as {@link SleepData} object.
     * Remember to call {@link #updateSleepData(android.content.Context)} first if needed.
     * @param context android context
     * @return data about last nights sleep
     * @throws BedditException
     */
    SleepData getSleepData(Context context) throws BedditException;

    /**
     * returns true if user has changed after the last call to {@link #updateSleepData(android.content.Context)}
     * @param context android context
     * @return true if user has changed after the last call to {@link #updateSleepData(android.content.Context)}
     */
    boolean hasUserChanged(Context context);

    /**
     *
     * @return true if sleep data has been updated more than 1 minutes ago
     */
    boolean isSleepInfoOutdated();

}
