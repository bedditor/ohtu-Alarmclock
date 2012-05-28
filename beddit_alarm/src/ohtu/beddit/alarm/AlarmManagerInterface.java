package ohtu.beddit.alarm;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 22.5.2012
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmManagerInterface {
    public void set(int type, long triggerAtTime, android.app.PendingIntent operation);

    public void setRepeating(int type, long triggerAtTime, long interval, android.app.PendingIntent operation);

    public void setInexactRepeating(int type, long triggerAtTime, long interval, android.app.PendingIntent operation);

    public void cancel(android.app.PendingIntent operation);

    public void setTime(long millis);

    public void setTimeZone(java.lang.String timeZone);
}
