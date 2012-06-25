package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;

/*
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

    public TimeDisplay(float x, float y, Paint p, boolean is24Hour) {
        this.x = x;
        this.y = y;
        this.p = p;
        this.is24Hour = is24Hour;
    }

    public void draw(Canvas c) {

        String time = timeToString(hours, minutes);
        float textLength = p.measureText(time);
        c.drawText(time, x - textLength / 2, y, p);
    }

    private String timeToString(int h, int m) {
        String minutes = minutesToString(m);
        String hours = hoursToString(h);
        String suffix = (is24Hour ? "" : (h > 11 ? " pm" : " am"));
        return hours + ":" + minutes + suffix;
    }

    private String minutesToString(int minutes) {
        return (minutes < 10 ? "0" + minutes : Integer.toString(minutes));
    }

    private String hoursToString(int hours) {
        if (is24Hour) {
            return hours < 10 ? "0"+hours : ""+hours;
        } else if (hours == 0 || hours == 12) {
            return "12";
        } else {
            return ""+(hours % 12);
        }
    }

    public void setIs24Hour(boolean b) {
        this.is24Hour = b;
    }

    @Override
    public void onHourChanged(int value) {
        this.hours = value;
    }

    @Override
    public void onMinuteChanged(int value) {
        this.minutes = value;
    }
}
