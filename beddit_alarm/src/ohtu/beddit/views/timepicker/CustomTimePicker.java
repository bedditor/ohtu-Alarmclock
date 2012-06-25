package ohtu.beddit.views.timepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import ohtu.beddit.alarm.AlarmTimeChangedListener;
import ohtu.beddit.alarm.AlarmTimePicker;

import java.util.LinkedList;
import java.util.List;

/**
 * A special view for choosing a time by manipulating the clock
 * hands on an analog clock face. Also includes a display of the currently
 * picked time and a slider for setting the alarm interval.
 */
public class CustomTimePicker extends View implements AlarmTimePicker, AnimationFinishedListener {
    // sizes as a fraction of the clock's radius:
    private final static float GRAB_POINT_SIZE = 0.1f;
    private final static float HAND_WIDTH = 0.02f;
    private final static float HOUR_HAND_LENGTH = 0.55f;
    private final static float CLOCK_NUMBER_SIZE = 0.2f;
    private final static float BAR_HEIGHT = 0.25f;
    private final static float BAR_WIDTH = 1.8f;
    // as a fraction of bar height:
    private final static float SPACER_SIZE = 0.2f;

    private final static int MAX_INTERVAL = 45;
    private final static int MINIMUM_SIZE = 100;

    private int initialMinutes = 0;
    private int initialHours = 0;
    private int initialInterval = 0;

    private boolean componentsCreated = false;
    private boolean enabled = true;
    private boolean is24Hour = true;

    private final List<AlarmTimeChangedListener> alarmTimeChangedListeners = new LinkedList<AlarmTimeChangedListener>();

    private Slider intervalSlider;
    private AnalogClock analogClock;
    private TimeDisplay timeDisplay;
    private MinuteHand minuteHand;
    private HourHand hourHand;

    private final List<Movable> movables = new LinkedList<Movable>();

    /**
     * Creates a new CustomTimePicker from xml.
     * @param context Android Context object.
     * @param attrs Set of attributes from the xml.
     */
    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePaints();
    }

    /**
     * Draws every component of the CustomTimePicker to the given Canvas.
     * @param c The Canvas object to draw to.
     */
    protected void onDraw(Canvas c) {
        analogClock.draw(c);
        minuteHand.draw(c);
        hourHand.draw(c);
        timeDisplay.draw(c);
        intervalSlider.draw(c);
    }

    /**
     * Responds to a touchscreen event.
     * Animates Movables in response to clicks and handles the dragging of Movables.
     * Reports the new time and interval to listeners when a drag ends.
     * @param me An object that describes a touchscreen event.
     * @return True if the touch event was handled.
     */
    public boolean onTouchEvent(MotionEvent me) {
        if (enabled) {
            boolean eventHandled = false;
            float x = me.getX(), y = me.getY();
            switch (me.getAction()) {
                case (MotionEvent.ACTION_UP):
                    for (Movable mv : movables) {
                        if (mv.wasClicked())
                            mv.startAnimation(mv.createTargetFromClick(x, y), this);
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

    private final Paint linePaint = new Paint();
    private final Paint timePaint = new Paint();
    private final Paint clockFaceLinePaint = new Paint();
    private final Paint backgroundPaint = new Paint();
    private final Paint intervalArcPaint = new Paint();

    /**
     * Initializes all the paints used by the CustomTimePicker to their default values.
     */
    private void initializePaints() {

        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);

        linePaint.setStyle(Paint.Style.STROKE);
        timePaint.setAntiAlias(true);
        timePaint.setColor(Color.BLACK);

        clockFaceLinePaint.setColor(Color.BLACK - (70 << 24));
        clockFaceLinePaint.setAntiAlias(true);

        intervalArcPaint.setColor(Color.argb(255, 255, 89, 0));
        backgroundPaint.setColor(Color.WHITE);

        intervalArcPaint.setAntiAlias(true);
        backgroundPaint.setAntiAlias(true);
    }

    /**
     * Initializes all the components of the CustomTimePicker
     */
    private void createComponents() {
        float radius = (getHeight() / 2) * (1 - BAR_HEIGHT);
        if (radius * 2 > getWidth())
            radius = getWidth() / 2;

        float barHeight = (radius * BAR_HEIGHT) * (1 - SPACER_SIZE);
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
        hourHand = new HourHand(midX, midY, initialHours, radius * HOUR_HAND_LENGTH, linePaint, grabPointSize, this);
        minuteHand = new MinuteHand(midX, midY, initialMinutes, radius, linePaint, grabPointSize, this, hourHand);
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

    /**
     *  Listens for component animations finishing and notifies listeners of changed alarm time and interval.
     */
    @Override
    public void onAnimationFinished() {
        for (AlarmTimeChangedListener l : alarmTimeChangedListeners)
            l.onAlarmTimeChanged(hourHand.getValue(), minuteHand.getValue(), intervalSlider.getValue());
    }

    /**
     * Overrides the base onSizeChanged method to create the view's components after the size of the view has been set.
     * @param w New width.
     * @param h New height.
     * @param oldw Old width.
     * @param oldh Old height.
     */
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

    /**
     * Sets the background color of the component.
     * @param c The new color.
     */
    @Override
    public void setBackgroundColor(int c) {
        backgroundPaint.setColor(c);
    }

    /**
     * Sets the foreground color of the component.
     * @param c The new Color.
     */
    @Override
    public void setForegroundColor(int c) {
        linePaint.setColor(c);
        timePaint.setColor(c);
        clockFaceLinePaint.setColor(c - (64 << 24));
    }

    /**
     * Sets the color of the interval arc.
     * @param c The new color.
     */
    @Override
    public void setSpecialColor(int c) {
        intervalArcPaint.setColor(c);
    }

    /**
     * Changes the time display format.
     * @param b True for 24-hour format, false for 12-hour format.
     */
    @Override
    public void set24HourMode(boolean b) {
        if (componentsCreated)
            timeDisplay.setIs24Hour(b);
        else
            is24Hour = b;
    }

    /**
     * Adds an AlarmTimeChangedListener to the CustomTimePicker.
     * @param l The listener to add.
     */
    @Override
    public void addAlarmTimeChangedListener(AlarmTimeChangedListener l) {
        alarmTimeChangedListeners.add(l);
    }
}
