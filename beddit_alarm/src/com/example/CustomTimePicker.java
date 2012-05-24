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
    int color = Color.MAGENTA;

    int xc = 0;
    int yc = 0;

    final float grabPointOffset = 0.2f;
    final float grabPointSize = 0.1f;
    final float handWidth = 5f;

    final double minuteIncrement = Math.PI / 30.0;
    final double hourIncrement = Math.PI / 6.0;

    final float hourHandLength = 0.5f;

    final int maxInterval = 30;
    float currentInterval = 0.0f;
    final float clockNumberSize = 50f;

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
    int interval = 0;

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
        intervalBarPaint.setColor(Color.GRAY);
        Paint intervalPickerPaint = new Paint();
        intervalPickerPaint.setColor(Color.RED);

        c.drawRect(intervalBarRect, intervalBarPaint);

        sliderGrabX = midX - radius + currentInterval * radius * 2;
        sliderGrabY = midY + radius + intervalBarRect.height() / 2;

        c.drawCircle(sliderGrabX, sliderGrabY, intervalBarRect.height() / 2, intervalPickerPaint);

        intervalPickerPaint.setStrokeWidth(50f);
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
        clockHand.setStrokeWidth(handWidth);
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
        c.drawCircle(midX,midY,handWidth/2,clockHand);
    }

    private void drawClockface(Canvas c) {
        Paint clockHourBackground = new Paint();
        Paint clockMinBackground = new Paint();
        Paint intervalArcPaint = new Paint();

        intervalArcPaint.setColor(Color.RED);
        clockHourBackground.setColor(Color.LTGRAY);
        clockHourBackground.setStyle(Paint.Style.STROKE);
        clockMinBackground.setColor(Color.WHITE);

        intervalArcPaint.setAntiAlias(true);
        clockHourBackground.setAntiAlias(true);
        clockMinBackground.setAntiAlias(true);

        c.drawArc(clockRect,
                (float) Math.toDegrees(getMinuteHandAngle() - Math.PI / 2),
                (float) Math.toDegrees(-minuteIncrement * (maxInterval * currentInterval)),
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

        linePaint.setTextSize(clockNumberSize);
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

    public boolean onTouchEvent(MotionEvent me) {
        switch (me.getAction()) {
            case (MotionEvent.ACTION_UP):
                hourGrabbed = false;
                minGrabbed = false;
                sliderGrabbed = false;
                break;
            case (MotionEvent.ACTION_DOWN):
                if (Math.abs(me.getX() - minGrabX) < radius * grabPointSize &&
                        Math.abs(me.getY() - minGrabY) < radius * grabPointSize) {
                    minGrabbed = true;
                } else if (Math.abs(me.getX() - hourGrabX) < radius * grabPointSize &&
                        Math.abs(me.getY() - hourGrabY) < radius * grabPointSize) {
                    hourGrabbed = true;
                } else if (Math.abs(me.getX() - sliderGrabX) < intervalBarRect.height() / 2 &&
                        Math.abs(me.getY() - sliderGrabY) < intervalBarRect.height() / 2) {
                    sliderGrabbed = true;
                }
                break;
            case (MotionEvent.ACTION_MOVE):
                double newAngle = Math.atan((me.getY() - midY) / (me.getX() - midX)) + Math.PI / 2;
                if (me.getX() - midX < 0)
                    newAngle += Math.PI;
                if (minGrabbed) updateMinuteHand(newAngle);
                else if (hourGrabbed) updateHourHand(newAngle);
                else if (sliderGrabbed) updateSlider(me.getX());
                break;
        }

        invalidate();
        return true;
    }

    private void updateSlider(float x) {
        if (x < intervalBarRect.left) currentInterval = 0f;
        else if (x > intervalBarRect.right) currentInterval = 1f;
        else {
            currentInterval = (x - intervalBarRect.left) / intervalBarRect.width();
        }

    }

    private void updateMinuteHand(double newAngle) {
        double minuteHandAngle = getMinuteHandAngle();
        if (newAngle - minuteHandAngle > Math.PI) {
            newAngle = (newAngle + Math.PI) % (2 * Math.PI);
            minuteHandAngle = (minuteHandAngle + Math.PI) % (2 * Math.PI);
        }

        double increments = ((newAngle - minuteHandAngle) / minuteIncrement);
        increments = Math.round(increments);
        if (Math.abs(newAngle-minuteHandAngle) > minuteIncrement) {
            Log.v("clock","incrementing mins by "+increments);
            minutes = (minutes + (int) increments);
            if (minutes >= 60) {
                minutes -= 60;
            } else if (minutes < 0)
                minutes += 60;
        }


    }

    private void updateHourHand(double newAngle) {
        double hourHandAngle = getHourHandAngle();

        if (newAngle - hourHandAngle > Math.PI) {
            newAngle = (newAngle + Math.PI) % (2 * Math.PI);
            hourHandAngle = (hourHandAngle + Math.PI) % (2 * Math.PI);
        }

        double increments = ((newAngle - hourHandAngle) / hourIncrement);
        increments = Math.round(increments);
        if (Math.abs(newAngle - hourHandAngle) > hourIncrement) {
            if (Math.abs(newAngle - hourHandAngle) > Math.PI)
                increments += 12;

            hours = (hours + (int) increments) % 24;
            Log.v("clock","incrementing hours by "+increments);
            if (hours < 0) hours += 24;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateSize();
    }

    private void updateSize() {
        int minDimension = Math.min(getWidth(), getHeight());
        int maxDimension = Math.max(getWidth(), getHeight());
        float barHeight = minDimension / 8;
        if (maxDimension > 1.25f * minDimension)
            radius = Math.min(getWidth(), getHeight()) / 2f;
        else
            radius = Math.min(getWidth(), getHeight()) / 2f - barHeight;
        midX = getWidth() / 2f;
        midY = getHeight() / 2f;
        intervalBarRect = new RectF(midX - radius, midY + radius, midX + radius, midY + radius + barHeight);
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
    public int getHours() {
        return hours;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public int getInterval() {
        return (int) (((float) maxInterval) * currentInterval);
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setInterval(int interval){

    }
}
