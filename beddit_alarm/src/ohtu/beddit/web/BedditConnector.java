package ohtu.beddit.web;

import android.content.Context;

import java.net.URL;

/**
 * ??? Please, add a description for this class.
 */
public interface BedditConnector {
    String getUserJson(Context context) throws BedditConnectionException;

    String getWakeUpJson(Context context, String date) throws BedditConnectionException;

    String getQueueStateJson(Context context, String date) throws BedditConnectionException;

    String requestDataAnalysis(Context context, String date) throws BedditConnectionException;

    String getJsonFromServer(Context context, String url, boolean doPost) throws BedditConnectionException;
}
