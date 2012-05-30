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

    public TimeDisplay(float x, float y, int hours, int minutes, Paint p) {
        this.x = x;
        this.y = y;
        this.hours = hours;
        this.minutes = minutes;
        this.p = p;
    }

    public void draw(Canvas c) {
        String time = timeToString(hours, minutes);
        float textLength = p.measureText(time);
        c.drawText(time, x - textLength / 2, y, p);
    }

    private String timeToString(int h, int m) {
        String hours = h < 10 ? "0" + h : Integer.toString(h);
        String minutes = m < 10 ? "0" + m : Integer.toString(m);
        return hours + ":" + minutes;
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
