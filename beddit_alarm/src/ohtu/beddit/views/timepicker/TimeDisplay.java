package ohtu.beddit.views.timepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import ohtu.beddit.R;
import ohtu.beddit.activity.MainActivity;

/**
 * A digital display of the time selected.
 * Time can be shown in 12- or 24-hour format.
 */
public class TimeDisplay implements HourChangedListener, MinuteChangedListener {
    private final float x;
    private final float y;
    private int hours;
    private int minutes;
    private final Paint p;
    private boolean is24Hour;
    private Context applicationContext;
    /**
     * Creates a new TimeDisplay object.
     * @param x Center x-coordinate.
     * @param y Bottom edge y-coordinate.
     * @param p Paint to use for drawing text.
     * @param is24Hour Indicates whether to use 24- or 12-hour format.
     */
    public TimeDisplay(float x, float y, Paint p, boolean is24Hour,
                       Context context) {
        this.applicationContext = context.getApplicationContext();
        this.x = x;
        this.y = y;
        this.p = p;
        this.is24Hour = is24Hour;
    }

    /**
     * Draws the time to the given canvas.
     * @param c Canvas object to draw to.
     */
    public void draw(Canvas c) {
        String time = timeToString(hours, minutes);
        float textLength = p.measureText(time);
        c.drawText(time, x - textLength / 2, y, p);
    }

    /**
     * Converts a minute and hour value into a time string.
     * @param h Hour value.
     * @param m Minute value.
     * @return Formatted time string.
     */
    private String timeToString(int h, int m) {
        String minutes = minutesToString(m);
        String hours = hoursToString(h);
        String suffix = "";
        if (!is24Hour) {
            if (h > 11) {
                suffix = " "+ applicationContext.getString(R.string.timepicker_pm);
            } else {
                suffix = " "+ applicationContext.getString(R.string.timepicker_am);
            }
        }
        return hours + ":" + minutes + suffix;
    }

    /**
     * Converts a minute value to string.
     * @param minutes Minute value.
     * @return Minute value as a string.
     */
    private String minutesToString(int minutes) {
        return (minutes < 10 ? "0" + minutes : Integer.toString(minutes));
    }

    /**
     * Converts a hour value to string.
     * @param hours Hour value.
     * @return Hour value as a string.
     */
    private String hoursToString(int hours) {
        if (is24Hour) {
            return hours < 10 ? "0"+hours : ""+hours;
        } else if (hours == 0 || hours == 12) {
            return "12";
        } else {
            return ""+(hours % 12);
        }
    }

    /**
     * Changes to/from 24 hour format.
     * @param b
     */
    public void setIs24Hour(boolean b) {
        this.is24Hour = b;
    }

    /**
     * Responds to a change in hour value.
     * @param value New hour value.
     */
    @Override
    public void onHourChanged(int value) {
        this.hours = value;
    }

    /**
     * Responds to a change in minute value
     * @param value New minute value.
     */
    @Override
    public void onMinuteChanged(int value) {
        this.minutes = value;
    }
}
