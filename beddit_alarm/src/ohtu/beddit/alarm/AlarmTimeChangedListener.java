package ohtu.beddit.alarm;

public interface AlarmTimeChangedListener {
    public void onAlarmTimeChanged(int hours, int minutes, int interval);
}
