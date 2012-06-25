package ohtu.beddit.views.timepicker;

/**
 * A simple interface for reporting a changed value.
 */
public interface ValueChangedListener {
    /**
     * Called when some important value for an object is changed
     * @param value the new value
     */
    public void onValueChanged(int value);
}
