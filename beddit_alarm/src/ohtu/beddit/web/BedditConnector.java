package ohtu.beddit.web;

import android.content.Context;

/**
 * This class is used for connecting to beddit api, getting json data from it and posting requests.
 */
public interface BedditConnector {
    String getUserJson(Context context) throws BedditConnectionException;

    String getWakeUpJson(Context context, String date) throws BedditConnectionException;

    String getQueueStateJson(Context context, String date) throws BedditConnectionException;

    String requestDataAnalysis(Context context, String date) throws BedditConnectionException;

    String getJsonFromServer(String url, boolean doPost) throws BedditConnectionException;
}
