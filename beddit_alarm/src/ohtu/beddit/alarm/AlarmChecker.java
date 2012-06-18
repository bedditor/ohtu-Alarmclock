package ohtu.beddit.alarm;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: Joza
 * Date: 28.5.2012
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmChecker {
    public boolean wakeUpNow(Context context);
    public int getWakeUpAttemptInterval(); //how often (seconds) we should try to wake up
    public int getCheckTime(); //how long (seconds) it may take to do the checking

}
