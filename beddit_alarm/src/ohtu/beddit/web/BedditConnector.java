package ohtu.beddit.web;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 7.6.2012
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public interface BedditConnector {
    String getUserJson(Context context) throws BedditConnectionException;

    String getWakeUpJson(Context context, String date) throws BedditConnectionException;

    String getQueueStateJson(Context context, String date) throws BedditConnectionException;

    String getSomeJson(Context context, String query, boolean do_post) throws BedditConnectionException;

    String requestDataAnalysis(Context context, String date) throws BedditConnectionException;

    String getAccessToken(String url) throws BedditConnectionException;
}
