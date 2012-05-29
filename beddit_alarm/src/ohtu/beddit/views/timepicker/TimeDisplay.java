package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class TimeDisplay {
    public float x;
    public float y;
    public float textSize;
    public int hours;
    public int minutes;

    public void draw(Canvas c) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);

        String time = timeToString(hours, minutes);
        float textLength = textPaint.measureText(time);

        c.drawText(time, x - textLength / 2, y, textPaint);
    }

    public String timeToString(int h, int m) {
        String hours = h < 10 ? "0" + h : Integer.toString(h);
        String minutes = m < 10 ? "0" + m : Integer.toString(m);
        return hours + ":" + minutes;
    }
}
