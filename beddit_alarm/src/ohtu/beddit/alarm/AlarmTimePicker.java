package ohtu.beddit.alarm;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 22.5.2012
 * Time: 10:18
 * To change this template use File | Settings | File Templates.
 */
public interface AlarmTimePicker {
    public int getHours();
    public int getMinutes();
    public int getInterval();
    public void setEnabled(boolean enabled);
    public boolean isEnabled();
}
