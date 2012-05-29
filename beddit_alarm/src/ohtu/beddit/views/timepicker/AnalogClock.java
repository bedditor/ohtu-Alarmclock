package ohtu.beddit.views.timepicker;

import android.graphics.*;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class AnalogClock implements SliderListener{

    private float x;
    private float y;
    private float radius;
    private float numberSize;
    private ClockHand minuteHand;
    private ClockHand hourHand;
    private int interval;

    public AnalogClock(float x, float y, float radius, float numberSize,
                       ClockHand minuteHand, ClockHand hourHand) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.numberSize = numberSize;
        this.minuteHand = minuteHand;
        this.hourHand = hourHand;
    }

    public boolean onHourArea(float x_, float y_) {
        return dist(x_,y_,x,y) < hourHand.getLength();
    }

    public boolean onMinuteArea(float x_, float y_) {
        return dist(x_,y_,x,y) < minuteHand.getLength();
    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    public void draw(Canvas c) {
        Paint clockHourBackground = new Paint();
        Paint clockMinBackground = new Paint();
        Paint intervalArcPaint = new Paint();

        intervalArcPaint.setColor(Color.CYAN);
        clockHourBackground.setColor(Color.LTGRAY);
        clockHourBackground.setStyle(Paint.Style.STROKE);
        clockMinBackground.setColor(Color.WHITE);

        intervalArcPaint.setAntiAlias(true);
        clockHourBackground.setAntiAlias(true);
        clockMinBackground.setAntiAlias(true);

        c.drawArc(new RectF(x-radius, y-radius, x+radius, y+radius),
                (float) Math.toDegrees(minuteHand.getAngle()),
                (float) Math.toDegrees(-(Math.PI / 30.0) * interval),
                true,
                intervalArcPaint);
        c.drawCircle(x,y, radius * 0.95f, clockMinBackground);
        c.drawCircle(x,y, hourHand.getLength(), clockHourBackground);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2);

        for (double m = 0; m < 60; m++) {
            double angle = Math.PI * 2 * m / 60;
            float xDir = (float) Math.cos(angle);
            float yDir = (float) Math.sin(angle);
            float len = m % 5 == 0 ? 0.9f : 0.95f;
            c.drawLine(x + xDir * radius * len * 0.95f,
                    y + yDir * radius * len * 0.95f,
                    x + xDir * radius * 0.95f,
                    y + yDir * radius * 0.95f, linePaint);
        }

        linePaint.setTextSize(numberSize);
        for (double h = 1; h < 13; h++) {
            double angle = Math.PI * 2 * h / 12 - Math.PI / 2;
            float xDir = (float) Math.cos(angle);
            float yDir = (float) Math.sin(angle);
            String text = Integer.toString((int) h);
            Rect textSize = new Rect(0,0,0,0);
            linePaint.getTextBounds(text, 0, text.length(), textSize);
            c.drawText(text,
                    x + xDir * radius * 0.7f - textSize.width() / 2,
                    y + yDir * radius * 0.7f + textSize.height() / 2,
                    linePaint);
        }

        hourHand.draw(c);
        minuteHand.draw(c);
    }

    @Override
    public void onValueChanged(int value) {
        interval = value;
    }
}
