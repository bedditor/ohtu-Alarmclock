package ohtu.beddit.views.timepicker;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ohtu.beddit.alarm.AlarmTimePicker;
import ohtu.beddit.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 21.5.2012
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimePicker extends View implements AlarmTimePicker {
    int minSize;

    // sizes as a fraction of the clock's radius
    private final static float GRAB_POINT_SIZE = 0.1f;
    private final static float HAND_WIDTH = 0.02f;
    private final static float HOUR_HAND_LENGTH = 0.55f;
    private final static float CLOCK_NUMBER_SIZE = 0.2f;
    private final static float BAR_HEIGHT = 0.25f;
    // as a fraction of bar height
    private final static float SPACER_SIZE = 0.2f;

    private final static double MINUTE_INCREMENT = Math.PI / 30.0;
    private final static double HOUR_INCREMENT = Math.PI / 6.0;

    private final static int MAX_INTERVAL = 45;

    private int initialMinutes = 0;
    private int initialHours = 0;
    private int initialInterval = 0;
    private boolean componentsCreated = false;
    private boolean locked = false;

    Slider intervalSlider;
    AnalogClock analogClock;
    TimeDisplay timeDisplay;
    MinuteHand minuteHand;
    HourHand hourHand;

    List<Movable> movables = new LinkedList<Movable>();

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.minSize = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/ohtu.beddit", "minSize"));
    }

    protected void onDraw(Canvas c) {
        analogClock.draw(c);
        minuteHand.draw(c);
        hourHand.draw(c);
        timeDisplay.draw(c);
        intervalSlider.draw(c);
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!locked) {
            boolean eventHandled = false;
            float x = me.getX(), y = me.getY();
            switch (me.getAction()) {
                case (MotionEvent.ACTION_UP):
                    for (Movable mv : movables) {
                        if (mv.wasClicked())
                            mv.animate(mv.createTargetFromClick(x, y));
                        mv.releaseClick();
                        mv.releaseGrab();
                    }
                    eventHandled = true;
                    break;
                case (MotionEvent.ACTION_DOWN):
                    for (Movable mv : movables)
                        if (eventHandled = mv.grab(x, y)) break;
                    for (Movable mv : movables)
                        if (eventHandled = mv.click(x, y)) break;
                    break;
                case (MotionEvent.ACTION_MOVE):
                    for (Movable mv : movables)
                        if (mv.isGrabbed())
                            mv.updatePositionFromClick(x, y);
                    eventHandled = true;
                    break;
            }
            invalidate();
            return eventHandled;
        } else {
            return false;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createComponents();
    }

    private void createComponents() {
        int minDimension = Math.min(getWidth(), getHeight());

        float radius = getHeight() >= (1 + BAR_HEIGHT) * getWidth() ?
                getWidth() * 0.5f : Math.min(getWidth(), getHeight()) * (0.5f - BAR_HEIGHT);

        float barHeight = (radius * BAR_HEIGHT)*(1-SPACER_SIZE);
        float barSpacer = barHeight * SPACER_SIZE;

        float midX = getWidth() / 2f;
        float midY = getHeight() / 2f;

        float grabPointSize = radius * GRAB_POINT_SIZE;

        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(radius * HAND_WIDTH);
        linePaint.setStyle(Paint.Style.STROKE);

        Paint timePaint = new Paint();
        timePaint.setAntiAlias(true);
        timePaint.setColor(Color.BLACK);
        timePaint.setTextSize(barHeight);

        Log.v("BAR HEIGHT", ""+barHeight);
        Log.v("SPACER SIZE", ""+barSpacer);
        Log.v("RADIUS", ""+radius);

        intervalSlider = new Slider(midX - radius * 0.9f, midY + radius + barSpacer,
                radius * 1.8f, barHeight, MAX_INTERVAL, initialInterval, linePaint, grabPointSize, this);
        hourHand = new HourHand(midX, midY, initialHours, HOUR_INCREMENT, radius * HOUR_HAND_LENGTH, linePaint, grabPointSize, this);
        minuteHand = new MinuteHand(midX, midY, initialMinutes, MINUTE_INCREMENT, radius, linePaint, grabPointSize, this, hourHand);
        analogClock = new AnalogClock(midX, midY, radius, radius * CLOCK_NUMBER_SIZE, minuteHand, hourHand);
        timeDisplay = new TimeDisplay(midX, midY - radius - barSpacer, initialHours, initialMinutes, timePaint);
        intervalSlider.addListener(analogClock);
        hourHand.addListener(timeDisplay);
        minuteHand.addListener(hourHand);
        minuteHand.addListener(timeDisplay);

        analogClock.onValueChanged(initialInterval);
        hourHand.onMinuteChanged(initialMinutes);

        movables.clear();
        movables.add(hourHand);
        movables.add(minuteHand);
        movables.add(intervalSlider);

        componentsCreated = true;
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
            result = minSize;
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
            result = minSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public int getHours() {
        if (componentsCreated)
            return hourHand.getValue();
        else
            return initialHours;
    }

    @Override
    public int getMinutes() {
        if (componentsCreated)
            return minuteHand.getValue();
        else
            return initialMinutes;
    }

    @Override
    public int getInterval() {
        if (componentsCreated)
            return intervalSlider.getValue();
        else
            return initialInterval;
    }

    @Override
    public void setHours(int hours) {
        if (componentsCreated)
            hourHand.setValue(hours);
        else
            initialHours = hours;
    }

    @Override
    public void setMinutes(int minutes) {
        if (componentsCreated)
            minuteHand.setValue(minutes);
        else
            initialMinutes = minutes;
    }

    @Override
    public void setInterval(int interval) {
        if (componentsCreated)
            intervalSlider.setValue(interval);
        else
            initialInterval = interval;
    }

    @Override
    public void setLocked(boolean lock) {
        this.locked = lock;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
