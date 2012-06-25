package ohtu.beddit.views.timepicker;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * A special clock hand that moves in increments of one hour on the clock face.
 * It listens to the minute hand so that its angle can be accurate at all times.
 */
public class HourHand extends ClockHand implements MinuteChangedListener {
    private static final int DEFAULT_MOVE_SPEED = 20;
    private int minuteValue = 0;
    private final List<HourChangedListener> listeners = new LinkedList<HourChangedListener>();
    public final static double INCREMENT = Math.PI / 6.0;

    /**
     * Creates a new hour hand object.
     * @param x Pivot point x-coordinate.
     * @param y Pivot point y-coordinate.
     * @param value Initial hour value.
     * @param length Hour hand length.
     * @param p Paint to draw the hour hand with.
     * @param grabPointSize Radius of the hour hand grab point in pixels.
     * @param parent Tha parent View of the hour hand.
     */
    public HourHand(float x, float y, int value, float length, Paint p,
                    float grabPointSize, View parent) {
        super(x, y, value, INCREMENT, length, p, grabPointSize, parent);
    }

    /**
     * Adds a HourChangedListener.
     * @param listener The listener to add.
     */
    public void addListener(HourChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * Listens to changes in the minute value.
     * @param value New minute value.
     */
    @Override
    public void onMinuteChanged(int value) {
        this.minuteValue = value;
    }

    /**
     * Sets the new hour value and informs listeners of change.
     * @param newValue New value for the clock hand.
     */
    @Override
    public void setValue(int newValue) {
        this.value = newValue;
        for (HourChangedListener hcl : listeners)
            hcl.onHourChanged(newValue);
    }

    /**
     * Gets the current angle of the hour hand.
     * @return Current hand angle in radians.
     */
    @Override
    public double getAngle() {
        return ((this.value % 12) * this.incrementSize) +
                (minuteValue / 60.0 * this.incrementSize);
    }

    /**
     * Increments the hour value.
     * @param inc Amount to increment the hour value by.
     */
    @Override
    public void incrementValue(int inc) {
        int val = getValue();
        val = (val + inc) % 24;
        if (val < 0) val += 24;
        setValue(val);
    }

    /**
     * Sets the clock hand angle.
     * @param newAngle The new hour hand angle.
     */
    @Override
    public void setAngle(double newAngle) {

        double diff = ClockHand.angleDiff(getAngle(), newAngle);
        if (Math.abs(diff) > this.incrementSize) {
            double increments = (diff / this.incrementSize);
            increments = Math.round(increments);
            incrementValue((int) increments);
        }
    }

    /**
     * Creates an Animator object to move the hand to a target hour value.
     * @param target The target hour value to move to.
     * @return The created Animator object.
     */
    @Override
    protected Animator createAnimator(int target) {
        return new Animator(parent, DEFAULT_MOVE_SPEED, target, this) {
            @Override
            public void animate() {
                int hours = movable.getValue();
                if ((hours % 12) < target) {
                    if (Math.abs((hours % 12) - target) > 6)
                        movable.incrementValue(-1);
                    else
                        movable.incrementValue(1);
                } else {
                    if (Math.abs((hours % 12) - target) > 6)
                        movable.incrementValue(1);
                    else
                        movable.incrementValue(-1);
                }
            }

            @Override
            public boolean notFinished() {
                return (movable.getValue() % 12) != getTarget();
            }
        };
    }
}