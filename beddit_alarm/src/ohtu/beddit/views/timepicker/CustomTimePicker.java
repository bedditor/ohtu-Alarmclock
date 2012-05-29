package ohtu.beddit.views.timepicker;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ohtu.beddit.alarm.AlarmTimePicker;
import ohtu.beddit.R;

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

    private final static float GRAB_POINT_OFFSET = 0.2f;
    private final static float GRAB_POINT_SIZE = 0.1f;
    private final static float HAND_WIDTH = 0.02f;
    private final static double MINUTE_INCREMENT = Math.PI / 30.0;
    private final static double HOUR_INCREMENT = Math.PI / 6.0;
    private final static float HOUR_HAND_LENGTH = 0.55f;
    private final static int MAX_INTERVAL = 45;
    private final static float CLOCK_NUMBER_SIZE = 0.2f;

    int currentInterval = 0;

    float radius = 0;

    Slider intervalSlider = new Slider();
    AnalogClock clockFace = new AnalogClock();
    TimeDisplay clockTime = new TimeDisplay();
    ClockHand minuteHand = new ClockHand();
    ClockHand hourHand = new ClockHand();

    int minutes = 0;
    int hours = 0;

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        for (int i = 0; i < attrs.getAttributeCount(); i++)
            Log.i("attribute", attrs.getAttributeName(i));

        this.minsize = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/ohtu.beddit", "minsize"));
        this.minutes = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/ohtu.beddit", "minutes"));
        this.hours = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/ohtu.beddit", "hours"));
        updateSize();
    }

    protected void onDraw(Canvas c) {
        clockFace.draw(c);
        minuteHand.draw(c);
        hourHand.draw(c);
        clockTime.draw(c);
        intervalSlider.draw(c);
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
                    minTarget = (int)(clockFace.getAngleToMidpoint(me.getX(), me.getY()) / MINUTE_INCREMENT);
                    if (!minAnimator.isAlive()) (minAnimator = new Thread(minRunnable)).start();

                } else if (hourClicking) {
                    Log.v("threadstart", "hour");
                    hourTarget = (int)(clockFace.getAngleToMidpoint(me.getX(), me.getY()) / HOUR_INCREMENT);
                    if (!hourAnimator.isAlive()) (hourAnimator = new Thread(hourRunnable)).start();
                } else if (sliderClicking) {
                    Log.v("threadstart", "interval");
                    sliderTarget  = (int)(((Math.min(intervalSlider.x + intervalSlider.w,
                                            Math.max(intervalSlider.x, me.getX())) - intervalSlider.x) /
                                        intervalSlider.w) * (float)intervalSlider.maxValue);
                    if (!sliderAnimator.isAlive()) (sliderAnimator = new Thread(sliderRunnable)).start();
                }

                hourGrabbed = minGrabbed = sliderGrabbed = false;
                minClicking = hourClicking = sliderClicking = false;
                break;
            case (MotionEvent.ACTION_DOWN):
                if (minuteHand.onGrabPoint(me.getX(), me.getY())) {
                    minGrabbed = true;
                } else if (hourHand.onGrabPoint(me.getX(), me.getY())) {
                    hourGrabbed = true;
                } else if (intervalSlider.onGrabPoint(me.getX(), me.getY())) {
                    Log.v("grabbing", "slider");
                    sliderGrabbed = true;
                } else if (intervalSlider.getRectF().contains(me.getX(),me.getY())) {
                    Log.v("clicking", "interval");
                    sliderClicking = true;
                } else if (clockFace.onHourArea(me.getX(), me.getY())) {
                    Log.v("clicking", "hours");
                    hourClicking = true;
                } else if (clockFace.onMinuteArea(me.getX(), me.getY())) {
                    Log.v("clicking", "minutes");
                    minClicking = true;
                }
                break;
            case (MotionEvent.ACTION_MOVE):
                double newAngle = clockFace.getAngleToMidpoint(me.getX(), me.getY());
                if (minGrabbed) updateMinuteHand(newAngle);
                else if (hourGrabbed) updateHourHand(newAngle);
                else if (sliderGrabbed) intervalSlider.update(me.getX());
                break;
        }

        invalidate();
        return true;
    }

    private double angleDiff(double a1, double a2) {
        double diff = a2 - a1;
        if (diff < -Math.PI) diff += Math.PI*2;
        else if (diff > Math.PI) diff -= Math.PI*2;
        return diff;
    }

    private void updateMinuteHand(double newAngle) {
        double diff = angleDiff(getMinuteHandAngle(), newAngle);
        if (Math.abs(diff) > MINUTE_INCREMENT) {
            double increments = (diff / MINUTE_INCREMENT);
            increments = Math.round(increments);

            Log.v("clock","incrementing mins by "+increments);

            incrementMinutes((int)increments);
        }
    }

    private void updateHourHand(double newAngle) {
        double diff = angleDiff(getHourHandAngle(), newAngle);
        if (Math.abs(diff) > HOUR_INCREMENT) {
            double increments = (diff / HOUR_INCREMENT);
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
        minuteHand.angle = getMinuteHandAngle() - Math.PI / 2;
        hourHand.angle = getHourHandAngle() - Math.PI / 2;
        clockFace.minuteHandAngle = minuteHand.angle;
        clockTime.minutes = minutes;
    }

    private void incrementHours(int increment) {
        hours = (hours + increment) % 24;
        if (hours < 0) hours += 24;

        hourHand.angle = getHourHandAngle() - Math.PI / 2;
        clockTime.hours = hours;
    }

    private double getHourHandAngle() {
        return ((hours % 12) * HOUR_INCREMENT) + (minutes / 60.0 * HOUR_INCREMENT);
    }

    private double getMinuteHandAngle() {
        return (minutes * MINUTE_INCREMENT);
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
        float midX = getWidth() / 2f;
        float midY = getHeight() / 2f;

        intervalSlider.x = midX - radius*0.9f;
        intervalSlider.y = midY + radius;
        intervalSlider.w = radius * 1.8f;
        intervalSlider.h = barHeight;
        intervalSlider.maxValue = MAX_INTERVAL;
        intervalSlider.grabPointRadius = radius * GRAB_POINT_SIZE;
        intervalSlider.strokeWidth = radius * HAND_WIDTH;

        hourHand.x = midX;
        hourHand.y = midY;
        hourHand.grabPointOffset = radius * GRAB_POINT_OFFSET;
        hourHand.grabPointSize = radius * GRAB_POINT_SIZE;
        hourHand.length = radius * HOUR_HAND_LENGTH;
        hourHand.strokeWidth = radius * HAND_WIDTH;
        hourHand.angle = -Math.PI / 2;

        minuteHand.x = midX;
        minuteHand.y = midY;
        minuteHand.grabPointOffset = radius * GRAB_POINT_OFFSET;
        minuteHand.grabPointSize = radius * GRAB_POINT_SIZE;
        minuteHand.length = radius;
        minuteHand.strokeWidth = radius * HAND_WIDTH;
        minuteHand.angle = -Math.PI / 2;

        clockFace.x = midX;
        clockFace.y = midY;
        clockFace.hourHandLength = hourHand.length;
        clockFace.numberSize = radius * CLOCK_NUMBER_SIZE;
        clockFace.radius = radius;

        clockTime.x = midX;
        clockTime.y = midY - radius;
        clockTime.textSize = intervalSlider.h;
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
