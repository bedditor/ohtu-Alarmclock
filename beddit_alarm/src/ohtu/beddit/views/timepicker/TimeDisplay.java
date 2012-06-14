package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class TimeDisplay implements HourChangedListener, MinuteChangedListener {
    private float x;
    private float y;
    private int hours;
    private int minutes;
    private Paint p;
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
        String minutes = (m < 10 ? "0" + m : Integer.toString(m));
        String hours = "";
        if (!is24Hour) {
            if (h == 0 || h == 12) {
                hours = "12";
            } else {
                hours += (h % 12);
            }
        } else {
            if (h < 10)
                hours += "0";
            hours += h;
        }
        return hours + ":" + minutes + (is24Hour ? "" : (h > 11 ? " pm" : " am"));
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
