package ohtu.beddit.alarm;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 4.6.2012
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmTimeChangedListener {
    public void onAlarmTimeChanged(int hours, int minutes, int interval);
}
