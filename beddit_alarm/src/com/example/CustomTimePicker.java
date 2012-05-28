package com.example;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 21.5.2012
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimePicker extends View implements AlarmTimePicker {
    Context context;
    int minsize;

    final float grabPointOffset = 0.2f;
    final float grabPointSize = 0.1f;
    final float handWidth = 0.02f;

    final double minuteIncrement = Math.PI / 30.0;
    final double hourIncrement = Math.PI / 6.0;

    final float hourHandLength = 0.55f;

    final int maxInterval = 45;
    int currentInterval = 0;
    final float clockNumberSize = 0.2f;

    float radius = 0;

    float hourGrabX;
    float minGrabX;
    float hourGrabY;
    float minGrabY;
    float sliderGrabX;
    float sliderGrabY;

    RectF intervalBarRect;
    RectF clockRect;

    float midX;
    float midY;

    int minutes = 0;
    int hours = 0;

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        for (int i = 0; i < attrs.getAttributeCount(); i++)
            Log.i("attribute", attrs.getAttributeName(i));

        this.minsize = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example", "minsize"));
        this.minutes = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example", "minutes"));
        this.hours = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example", "hours"));
        updateSize();
    }

    protected void onDraw(Canvas c) {
        drawClockface(c);
        drawClockHands(c);
        drawClockTime(c);
        drawIntervalBar(c);
    }

    private void drawIntervalBar(Canvas c) {
        Paint intervalBarPaint = new Paint();
        intervalBarPaint.setColor(Color.BLACK);

        intervalBarPaint.setStyle(Paint.Style.STROKE);
        intervalBarPaint.setStrokeWidth(handWidth*radius);
        intervalBarPaint.setAntiAlias(true);

        c.drawLine(intervalBarRect.left,
                   intervalBarRect.centerY(),
                   intervalBarRect.right,
                   intervalBarRect.centerY(),
                   intervalBarPaint);

        sliderGrabX = intervalBarRect.left + ((float)currentInterval)/((float)maxInterval) * intervalBarRect.width();
        sliderGrabY = intervalBarRect.centerY();

        c.drawCircle(sliderGrabX, sliderGrabY, radius*grabPointSize, intervalBarPaint);
    }

    private void drawClockTime(Canvas c) {
        Paint textPaint = new Paint();
        textPaint.setTextSize(intervalBarRect.height());
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);

        String time = timeToString(hours, minutes);
        float textLength = textPaint.measureText(time);

        c.drawText(time, midX - textLength / 2, midY - radius, textPaint);
    }

    private void drawClockHands(Canvas c) {
        Paint clockHand = new Paint();
        clockHand.setColor(Color.BLACK);
        clockHand.setAntiAlias(true);
        clockHand.setStrokeWidth(handWidth*radius);
        clockHand.setStyle(Paint.Style.STROKE);

        double hxDir = Math.cos(getHourHandAngle() - Math.PI / 2);
        double hyDir = Math.sin(getHourHandAngle() - Math.PI / 2);
        double mxDir = Math.cos(getMinuteHandAngle() - Math.PI / 2);
        double myDir = Math.sin(getMinuteHandAngle() - Math.PI / 2);

        float hx = (float) hxDir * radius * hourHandLength;
        float hy = (float) hyDir * radius * hourHandLength;
        float mx = (float) mxDir * radius;
        float my = (float) myDir * radius;

        minGrabX = midX + mx - (float)(mxDir * radius * grabPointOffset);
        minGrabY = midY + my - (float)(myDir * radius * grabPointOffset);

        hourGrabX = midX + hx - (float)(hxDir * radius * grabPointOffset);
        hourGrabY = midY + hy - (float)(hyDir * radius * grabPointOffset);

        c.drawLine(midX, midY, midX + hx, midY + hy, clockHand);
        c.drawLine(midX, midY, midX + mx, midY + my, clockHand);

        c.drawCircle(minGrabX, minGrabY, radius * grabPointSize, clockHand);
        c.drawCircle(hourGrabX, hourGrabY, radius * grabPointSize, clockHand);
        clockHand.setStyle(Paint.Style.FILL);
        c.drawCircle(midX,midY,handWidth*radius/2,clockHand);
    }

    private void drawClockface(Canvas c) {
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

        c.drawArc(clockRect,
                (float) Math.toDegrees(getMinuteHandAngle() - Math.PI / 2),
                (float) Math.toDegrees(-minuteIncrement * currentInterval),
                true,
                intervalArcPaint);
        c.drawCircle(midX, midY, radius * 0.95f, clockMinBackground);
        c.drawCircle(midX, midY, radius * hourHandLength, clockHourBackground);

        Paint linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2);
        for (double m = 0; m < 60; m++) {
            double angle = Math.PI * 2 * m / 60;
            float xDir = (float) Math.cos(angle);
            float yDir = (float) Math.sin(angle);
            float len = m % 5 == 0 ? 0.9f : 0.95f;
            c.drawLine(midX + xDir * radius * len * 0.95f,
                    midY + yDir * radius * len * 0.95f,
                    midX + xDir * radius * 0.95f,
                    midY + yDir * radius * 0.95f, linePaint);
        }

        linePaint.setTextSize(clockNumberSize*radius);
        for (double h = 1; h < 13; h++) {
            double angle = Math.PI * 2 * h / 12 - Math.PI / 2;
            float xDir = (float) Math.cos(angle);
            float yDir = (float) Math.sin(angle);
            String text = Integer.toString((int) h);
            Rect textSize = new Rect(0,0,0,0);
            linePaint.getTextBounds(text, 0, text.length(), textSize);
            c.drawText(text,
                    midX + xDir * radius * 0.7f - textSize.width() / 2,
                    midY + yDir * radius * 0.7f + textSize.height() / 2,
                    linePaint);
        }

    }

    public String timeToString(int h, int m) {
        String hours = h < 10 ? "0" + h : Integer.toString(h);
        String minutes = m < 10 ? "0" + m : Integer.toString(m);
        return hours + ":" + minutes;
    }


    private double getHourHandAngle() {
        return ((hours % 12) * hourIncrement) + (minutes / 60.0 * hourIncrement);
    }

    private double getMinuteHandAngle() {
        return (minutes * minuteIncrement);
    }

    boolean hourGrabbed = false;
    boolean minGrabbed = false;
    boolean sliderGrabbed = false;

    boolean hourClicking = false;
    boolean minClicking = false;
    boolean sliderClicking = false;

    int minTarget = 0;
    int hourTarget = 0;
    int sliderTarget = 0;
    boolean animating = false;

    Thread minAnimator = new Thread();
    Thread hourAnimator = new Thread();
    Thread sliderAnimator = new Thread();

    final View v = findViewById(R.id.alarmTimePicker);
    Runnable invalidator = new Runnable(){ public void run(){ v.invalidate(); } };
    Runnable minRunnable = new Runnable() {
            public void run() {
                while (minutes != minTarget) {
                    if (minutes < minTarget)  {
                        if (Math.abs(minutes - minTarget) > 30)
                            incrementMinutes(-1);
                        else
                            incrementMinutes(1);
                    } else {
                        if (Math.abs(minutes - minTarget) > 30)
                            incrementMinutes(1);
                        else
                            incrementMinutes(-1);
                    }
                    v.post(invalidator);
                    try { Thread.sleep(5); } catch (Throwable t) {}
                }
            }};
    Runnable hourRunnable = new Runnable() {
        public void run() {
            while ((hours%12) != hourTarget) {
                if ((hours%12) < hourTarget)  {
                    if (Math.abs((hours%12) - hourTarget) > 6)
                        incrementHours(-1);
                    else
                        incrementHours(1);
                } else {
                    if (Math.abs((hours%12) - hourTarget) > 6)
                        incrementHours(1);
                    else
                        incrementHours(-1);
                }
                v.post(invalidator);
                try { Thread.sleep(20); } catch (Throwable t) {}
            }
        }};
    Runnable sliderRunnable = new Runnable() {
        public void run() {
            while (currentInterval != sliderTarget) {
                if (currentInterval < sliderTarget) currentInterval++; else currentInterval--;
                v.post(invalidator);
                try { Thread.sleep(5); } catch (Throwable t) {}
            }
        }};

    public boolean onTouchEvent(MotionEvent me) {
        switch (me.getAction()) {
            case (MotionEvent.ACTION_UP):
                if (minClicking) {
                    Log.v("threadstart", "min");
                    minTarget = (int)(getAngleToMidpoint(me.getX(), me.getY()) / minuteIncrement);
                    if (!minAnimator.isAlive()) (minAnimator = new Thread(minRunnable)).start();

                } else if (hourClicking) {
                    Log.v("threadstart", "hour");
                    hourTarget = (int)(getAngleToMidpoint(me.getX(), me.getY()) / hourIncrement);
                    if (!hourAnimator.isAlive()) (hourAnimator = new Thread(hourRunnable)).start();
                } else if (sliderClicking) {
                    Log.v("threadstart", "interval");
                    sliderTarget  = (int)(((Math.min(intervalBarRect.right,
                                                    Math.max(intervalBarRect.left, me.getX())) - intervalBarRect.left) /
                                        intervalBarRect.width()) * (float)maxInterval);
                    Log.v("slider x", Float.toString(me.getX()));
                    Log.v("sliderbar left", Float.toString(intervalBarRect.left));
                    Log.v("sliderbar right", Float.toString(intervalBarRect.right));
                    Log.v("slider target", Float.toString(sliderTarget));
                    if (!sliderAnimator.isAlive()) (sliderAnimator = new Thread(sliderRunnable)).start();
                }

                hourGrabbed = minGrabbed = sliderGrabbed = false;
                minClicking = hourClicking = sliderClicking = false;
                break;
            case (MotionEvent.ACTION_DOWN):
                if (dist(me.getX(),me.getY(),minGrabX,minGrabY) < grabPointSize*radius*2) {
                    minGrabbed = true;
                } else if (dist(me.getX(),me.getY(),hourGrabX,hourGrabY) < grabPointSize*radius*2) {
                    hourGrabbed = true;
                } else if (dist(me.getX(),me.getY(),sliderGrabX,sliderGrabY) < grabPointSize*radius*2) {
                    sliderGrabbed = true;
                } else if (intervalBarRect.contains(me.getX(),me.getY())) {
                    Log.v("clicking", "interval");
                    sliderClicking = true;
                } else if (dist(me.getX(),me.getY(),midX,midY) < radius*hourHandLength) {
                    Log.v("clicking", "hours");
                    hourClicking = true;
                } else if (dist(me.getX(),me.getY(),midX,midY) < radius) {
                    Log.v("clicking", "minutes");
                    minClicking = true;
                }
                break;
            case (MotionEvent.ACTION_MOVE):
                double newAngle = getAngleToMidpoint(me.getX(), me.getY());
                if (minGrabbed) updateMinuteHand(newAngle);
                else if (hourGrabbed) updateHourHand(newAngle);
                else if (sliderGrabbed) updateSlider(me.getX());
                break;
        }

        invalidate();
        return true;
    }

    private double getAngleToMidpoint(float x, float y) {
        double angle = Math.atan((y - midY) / (x - midX)) + Math.PI / 2;
        if (x - midX < 0)
            angle += Math.PI;
        return angle;
    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    private void updateSlider(float x) {
        if (x < intervalBarRect.left) currentInterval = 0;
        else if (x > intervalBarRect.right) currentInterval = maxInterval;
        else {
            currentInterval = (int)(((x - intervalBarRect.left)/intervalBarRect.width())*(float)maxInterval);
        }

    }

    private double angleDiff(double a1, double a2) {
        double diff = a2 - a1;
        if (diff < -Math.PI) diff += Math.PI*2;
        else if (diff > Math.PI) diff -= Math.PI*2;
        return diff;
    }

    private void updateMinuteHand(double newAngle) {
        double diff = angleDiff(getMinuteHandAngle(), newAngle);
        if (Math.abs(diff) > minuteIncrement) {
            double increments = (diff / minuteIncrement);
            increments = Math.round(increments);

            Log.v("clock","incrementing mins by "+increments);

            incrementMinutes((int)increments);
        }
    }

    private void updateHourHand(double newAngle) {
        double diff = angleDiff(getHourHandAngle(), newAngle);
        if (Math.abs(diff) > hourIncrement) {
            double increments = (diff / hourIncrement);
            increments = Math.round(increments);

            Log.v("clock","incrementing hours by "+increments);
            incrementHours((int) increments);
        }
    }

    public void incrementMinutes(int increment) {
        minutes += increment;
        if (minutes >= 60) {
            minutes %= 60;
            incrementHours(1);
        } else if (minutes < 0) {
            minutes += 60;
            incrementHours(-1);
        }
    }

    private void incrementHours(int increment) {
        hours = (hours + increment) % 24;
        if (hours < 0) hours += 24;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateSize();
    }

    private void updateSize() {
        int minDimension = Math.min(getWidth(), getHeight());
        float barHeight = minDimension / 8;
        if (getHeight() >= 1.25f * getWidth())
            radius = getWidth() / 2f;
        else
            radius = Math.min(getWidth(), getHeight()) / 2f - barHeight;
        midX = getWidth() / 2f;
        midY = getHeight() / 2f;
        intervalBarRect = new RectF(midX - radius*0.9f, midY + radius, midX + radius*0.9f, midY + radius + barHeight);
        clockRect = new RectF(midX - radius, midY - radius, midX + radius, midY + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = minsize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = minsize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public int getHours() { return hours; }

    @Override
    public int getMinutes() { return minutes; }

    @Override
    public int getInterval() { return currentInterval; }

    public void setHours(int hours) { this.hours = hours; }

    public void setMinutes(int minutes) { this.minutes = minutes; }

    public void setInterval(int interval){ currentInterval = interval; }
}
