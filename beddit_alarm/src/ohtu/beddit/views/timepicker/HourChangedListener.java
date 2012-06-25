package ohtu.beddit.views.timepicker;

/**
 * A simple interface to allow the hour hand to report changes in its value
 */
public interface HourChangedListener {
    /**
     * Responds to a change in the selected hour value.
     * @param value New hour value.
     */
    public void onHourChanged(int value);
}
