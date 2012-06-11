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
    String getUserJson(Context context) throws MalformedBedditJsonException;

    String getWakeUpJson(Context context) throws MalformedBedditJsonException;

    String getSomeJson(Context context, String query) throws MalformedBedditJsonException;
}
