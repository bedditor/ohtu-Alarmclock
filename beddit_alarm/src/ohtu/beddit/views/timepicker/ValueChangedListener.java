package ohtu.beddit.views.timepicker;

/**
 * A simple interface for reporting a changed value.
 */
public interface ValueChangedListener {
    /**
     * Responds to a change in some value.
     * @param value the new value
     */
    public void onValueChanged(int value);
}
