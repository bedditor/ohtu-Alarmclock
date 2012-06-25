package ohtu.beddit.views.timepicker;

/*
 * A simple interface to allow the minute hand to report changes in its value
 */
public interface MinuteChangedListener {
    public void onMinuteChanged(int value);
}
