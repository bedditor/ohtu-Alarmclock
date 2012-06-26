package ohtu.beddit.web;

import android.content.Context;

/**
 * This class is used for connecting to beddit api, getting json data from it and posting requests.
 */
public interface BedditConnector {
    /**
     * Queries api for user data.
     * @param context android context
     * @return user data in json
     * @throws BedditConnectionException
     */
    String getUserJson(Context context) throws BedditConnectionException;

    /**
     * Queries api for the last night sleep data.
     * @param context android context
     * @return sleep data in json
     * @throws BedditConnectionException
     */
    String getWakeUpJson(Context context, String date) throws BedditConnectionException;

    /**
     * Queries api for the current state of sleep data computation
     * @param context android context
     * @return queue status in json
     * @throws BedditConnectionException
     */
    String getQueueStateJson(Context context, String date) throws BedditConnectionException;

    /**
     * Sends a post-request to api asking for the calculation of the latest sleep data
     * @param context android context
     * @param date Which date to compute. Should normally be current date.
     * @return server response
     * @throws BedditConnectionException
     */
    String requestDataAnalysis(Context context, String date) throws BedditConnectionException;

    /**
     * Connects to beddit server and sends https request to given url. Returns https response.
     * @param url url for api-request
     * @param doPost true if HTTP post, false if HTTP get
     * @return response from server
     * @throws BedditConnectionException
     */
    String getJsonFromServer(String url, boolean doPost) throws BedditConnectionException;
}
