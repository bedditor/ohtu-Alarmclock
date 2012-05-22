package com.example;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 21.5.2012
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimePicker extends View {
    Context context;
    int size;
    int color = Color.MAGENTA;
    Random r = new Random();
    int xc = 0;
    int yc = 0;

    final float grabPointOffset = 0.85f;
    final float grabPointSize = 0.1f;

    final double minuteAngle = Math.PI / 30.0;
    final double hourAngle = Math.PI / 6.0;

    float minHand = 0f;
    float hourHand = 0f;

    float radius = 0;

    float hourGrabX;
    float minGrabX;
    float hourGrabY;
    float minGrabY;

    float midX;
    float midY;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        updateSize();
    }

    private void updateSize() {
        midX = getWidth() / 2f;
        midY = getHeight() / 2f;
        radius = Math.min(getWidth(), getHeight()) / 2f;
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        for(int i=0; i < attrs.getAttributeCount(); i++)
            Log.i("attribute", attrs.getAttributeName(i));

        this.size = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example", "size"));
        updateSize();
    }


    protected void onDraw(Canvas c) {

        Paint clockHand = new Paint();
        Paint clockHourBackground = new Paint();
        Paint clockMinBackground = new Paint();

        clockHourBackground.setColor(Color.GRAY);
        clockMinBackground.setColor(Color.LTGRAY);
        clockHand.setColor(Color.BLACK);

        c.drawCircle(midX,midY,radius,clockMinBackground);
        c.drawCircle(midX,midY,radius*0.75f,clockHourBackground);

        float hx = (float)Math.cos(hourHand-Math.PI/2) * radius * 0.75f;
        float hy = (float)Math.sin(hourHand-Math.PI/2) * radius * 0.75f;
        float mx = (float)Math.cos(minHand-Math.PI/2) * radius;
        float my = (float)Math.sin(minHand-Math.PI/2) * radius;

        minGrabX = midX+mx*grabPointOffset;
        minGrabY = midY+my*grabPointOffset;
        hourGrabX = midX+hx*grabPointOffset;
        hourGrabY = midY+hy*grabPointOffset;

        c.drawLine(midX,midY,midX+hx,midY+hy,clockHand);
        c.drawLine(midX,midY,midX+mx,midY+my,clockHand);
        c.drawCircle(minGrabX, minGrabY, radius*grabPointSize, clockHand);
        c.drawCircle(hourGrabX, hourGrabY, radius*grabPointSize, clockHand);

        Paint textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);

        String time =  getHours()+":"+getMinutes();
        float textLength = textPaint.measureText(time);

        c.drawText(time,midX-textLength/2,midY,textPaint);
    }

    boolean hourGrabbed = false;
    boolean minGrabbed = false;

    public int getHours() {
        int hours = (int)Math.round(hourHand / hourAngle);
        return hours == 0 ? 12 : hours;
    }

    public int getMinutes() {
        return (int)Math.round(minHand / minuteAngle);
    }

    public boolean onTouchEvent(MotionEvent me) {
        switch (me.getAction()) {
            case (MotionEvent.ACTION_UP):
                hourGrabbed = false;
                minGrabbed = false;
                break;
            case (MotionEvent.ACTION_DOWN):
                if (Math.abs(me.getX() - minGrabX) < radius*grabPointSize &&
                    Math.abs(me.getY() - minGrabY) < radius*grabPointSize) {
                    minGrabbed = true;
                } else if (Math.abs(me.getX() - hourGrabX) < radius*grabPointSize &&
                        Math.abs(me.getY() - hourGrabY) < radius*grabPointSize) {
                    hourGrabbed = true;
                }
                break;
            case (MotionEvent.ACTION_MOVE):
                float newY = me.getHistoricalY(0);
                float newX = me.getHistoricalX(0);
                double newAngle = Math.atan((newY-midY)/(newX-midX));
                if (newX-midX < 0)
                    newAngle += Math.PI;
                if (minGrabbed) updateMinuteHand(newAngle);
                else if (hourGrabbed) updateHourHand(newAngle);

                break;
        }

        invalidate();
        return true;
    }

    private void updateMinuteHand(double newAngle) {
        double minutes = (newAngle / minuteAngle);
        minutes = Math.floor(minutes);
        minHand = (float)(minutes * minuteAngle+Math.PI/2);
    }

    private void updateHourHand(double newAngle) {
        double hours = (newAngle / hourAngle);
        hours = Math.floor(hours);
        hourHand = (float)(hours * hourAngle+Math.PI/2);
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
            result = size;
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
            result = size;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
