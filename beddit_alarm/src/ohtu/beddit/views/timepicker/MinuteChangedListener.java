package ohtu.beddit.views.timepicker;

/**
 * A simple interface to allow the minute hand to report changes in its value
 */
public interface MinuteChangedListener {
    /**
     * Responds to a change in the selected minute value.
     * @param value New minute value.
     */
    public void onMinuteChanged(int value);
}
