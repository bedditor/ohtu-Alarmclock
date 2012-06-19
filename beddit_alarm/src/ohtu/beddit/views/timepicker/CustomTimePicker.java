package ohtu.beddit.views.timepicker;

import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ohtu.beddit.alarm.AlarmTimeChangedListener;
import ohtu.beddit.alarm.AlarmTimePicker;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 21.5.2012
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimePicker extends View implements AlarmTimePicker, AnimationFinishedListener {
    int minSize;

    // sizes as a fraction of the clock's radius
    private final static float GRAB_POINT_SIZE = 0.1f;
    private final static float HAND_WIDTH = 0.02f;
    private final static float HOUR_HAND_LENGTH = 0.55f;
    private final static float CLOCK_NUMBER_SIZE = 0.2f;
    private final static float BAR_HEIGHT = 0.25f;
    private final static float BAR_WIDTH = 1.8f;
    // as a fraction of bar height
    private final static float SPACER_SIZE = 0.2f;

    private final static double MINUTE_INCREMENT = Math.PI / 30.0;
    private final static double HOUR_INCREMENT = Math.PI / 6.0;

    private final static int MAX_INTERVAL = 45;

    private int initialMinutes = 0;
    private int initialHours = 0;
    private int initialInterval = 0;
    private boolean componentsCreated = false;
    private boolean enabled = true;
    private boolean is24Hour = true;

    private List<AlarmTimeChangedListener> alarmTimeChangedListeners = new LinkedList<AlarmTimeChangedListener>();

    Slider intervalSlider;
    AnalogClock analogClock;
    TimeDisplay timeDisplay;
    MinuteHand minuteHand;
    HourHand hourHand;

    List<Movable> movables = new LinkedList<Movable>();

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.minSize = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res/ohtu.beddit", "minSize"));
        initializePaints();
    }

    protected void onDraw(Canvas c) {
        analogClock.draw(c);
        minuteHand.draw(c);
        hourHand.draw(c);
        timeDisplay.draw(c);
        intervalSlider.draw(c);
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (enabled) {
            boolean eventHandled = false;
            float x = me.getX(), y = me.getY();
            switch (me.getAction()) {
                case (MotionEvent.ACTION_UP):
                    for (Movable mv : movables) {
                        if (mv.wasClicked())
                            mv.animate(mv.createTargetFromClick(x, y), this);
                        mv.releaseClick();
                        if (mv.isGrabbed()) {
                            mv.releaseGrab();
                            for (AlarmTimeChangedListener l : alarmTimeChangedListeners)
                                l.onAlarmTimeChanged(hourHand.getValue(), minuteHand.getValue(), intervalSlider.getValue());
                        }
                    }
                    eventHandled = true;
                    break;
                case (MotionEvent.ACTION_DOWN):
                    for (Movable mv : movables)
                        if (mv.grab(x, y)) return true;
                    for (Movable mv : movables)
                        if (mv.click(x, y)) return true;
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

    Paint linePaint = new Paint();
    Paint timePaint = new Paint();
    Paint clockFaceLinePaint = new Paint();
    Paint backgroundPaint = new Paint();
    Paint intervalArcPaint = new Paint();

    private void initializePaints() {

        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);

        linePaint.setStyle(Paint.Style.STROKE);
        timePaint.setAntiAlias(true);
        timePaint.setColor(Color.BLACK);

        clockFaceLinePaint.setColor(Color.BLACK - (70 << 24));
        clockFaceLinePaint.setAntiAlias(true);

        intervalArcPaint.setColor(Color.argb(255,255,89,0));
        backgroundPaint.setColor(Color.WHITE);

        intervalArcPaint.setAntiAlias(true);
        backgroundPaint.setAntiAlias(true);
    }

    private void createComponents() {
        float radius = (getHeight() / 2) * (1-BAR_HEIGHT);
        if (radius*2 > getWidth())
            radius = getWidth() / 2;

        float barHeight = (radius * BAR_HEIGHT)*(1-SPACER_SIZE);
        float barSpacer = barHeight * SPACER_SIZE;
        float midX = getWidth() / 2f;
        float midY = getHeight() / 2f;
        float grabPointSize = radius * GRAB_POINT_SIZE;

        // update scaled paint attributes
        linePaint.setStrokeWidth(radius * HAND_WIDTH);
        timePaint.setTextSize(barHeight);
        clockFaceLinePaint.setTextSize(radius * CLOCK_NUMBER_SIZE);

        // create individual view components
        intervalSlider = new Slider(midX - radius * BAR_WIDTH / 2, midY + radius + barSpacer,
                radius * BAR_WIDTH, barHeight, MAX_INTERVAL, initialInterval, linePaint, grabPointSize, this);
        hourHand = new HourHand(midX, midY, initialHours, HOUR_INCREMENT, radius * HOUR_HAND_LENGTH, linePaint, grabPointSize, this);
        minuteHand = new MinuteHand(midX, midY, initialMinutes, MINUTE_INCREMENT, radius, linePaint, grabPointSize, this, hourHand);
        analogClock = new AnalogClock(midX, midY, radius,
                intervalArcPaint, backgroundPaint, clockFaceLinePaint,
                minuteHand, hourHand);
        timeDisplay = new TimeDisplay(midX, midY - radius - barSpacer, timePaint, is24Hour);

        // link components together
        intervalSlider.addListener(analogClock);
        hourHand.addListener(timeDisplay);
        minuteHand.addListener(hourHand);
        minuteHand.addListener(timeDisplay);

        // make sure we have the right initial values
        analogClock.onValueChanged(initialInterval);
        hourHand.onMinuteChanged(initialMinutes);
        timeDisplay.onHourChanged(initialHours);
        timeDisplay.onMinuteChanged(initialMinutes);

        movables.clear();
        movables.add(hourHand);
        movables.add(minuteHand);
        movables.add(intervalSlider);

        componentsCreated = true;
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
    public void onAnimationFinished() {
        for (AlarmTimeChangedListener l : alarmTimeChangedListeners)
            l.onAlarmTimeChanged(hourHand.getValue(), minuteHand.getValue(), intervalSlider.getValue());
    }

    //<editor-fold overrides>

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createComponents();
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setBackgroundColor(int c) {
        backgroundPaint.setColor(c);
    }

    @Override
    public void setForegroundColor(int c) {
        linePaint.setColor(c);
        timePaint.setColor(c);
        clockFaceLinePaint.setColor(c - (64 << 24));
    }

    @Override
    public void setSpecialColor(int c) {
        intervalArcPaint.setColor(c);
    }

    @Override
    public void set24HourMode(boolean b) {
        if (componentsCreated)
            timeDisplay.setIs24Hour(b);
        else
            is24Hour = b;
    }

    @Override
    public void addAlarmTimeChangedListener(AlarmTimeChangedListener l) {
        alarmTimeChangedListeners.add(l);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            setMinutes(bundle.getInt("minutes"));
            setHours(bundle.getInt("hours"));
            setInterval(bundle.getInt("interval"));
            super.onRestoreInstanceState(bundle.getParcelable("state"));
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt("minutes", getMinutes());
        bundle.putInt("hours", getHours());
        bundle.putInt("interval", getInterval());
        bundle.putParcelable("state", super.onSaveInstanceState());
        return bundle;
    }

    //</editor-fold>
}
