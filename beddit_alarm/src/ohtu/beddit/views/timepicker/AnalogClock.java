package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/*
 * The analog clock face part of CustomTimePicker.
 * Contains two clock hands and listens to the interval
 * picking slider.
 */
public class AnalogClock implements ValueChangedListener {

    private final float x;
    private final float y;
    private final float radius;
    private final ClockHand minuteHand;
    private final ClockHand hourHand;
    private int interval;
    private final Paint intervalArcPaint;
    private final Paint backgroundPaint;
    private final Paint linePaint;


    public AnalogClock(float x, float y, float radius,
                       Paint intervalArcPaint, Paint backgroundPaint, Paint linePaint,
                       ClockHand minuteHand, ClockHand hourHand) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.minuteHand = minuteHand;
        this.hourHand = hourHand;
        this.intervalArcPaint = intervalArcPaint;
        this.backgroundPaint = backgroundPaint;
        this.linePaint = linePaint;
    }

    public void draw(Canvas c) {
        c.drawArc(new RectF(x - radius, y - radius, x + radius, y + radius),
                (float) Math.toDegrees(minuteHand.getAngle() - Math.PI / 2),
                (float) Math.toDegrees(-(Math.PI / 30.0) * interval),
                true,
                intervalArcPaint);

        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.STROKE);

        c.drawCircle(x, y, radius * 0.95f, backgroundPaint);
        c.drawCircle(x, y, hourHand.getLength(), linePaint);

        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.FILL);


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


        for (double h = 1; h < 13; h++) {
            double angle = Math.PI * 2 * h / 12 - Math.PI / 2;
            float xDir = (float) Math.cos(angle);
            float yDir = (float) Math.sin(angle);
            String text = Integer.toString((int) h);
            Rect textSize = new Rect(0, 0, 0, 0);
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
