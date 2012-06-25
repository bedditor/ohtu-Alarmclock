package ohtu.beddit.views.timepicker;

import android.graphics.Paint;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * A special clock hand that moves in one minute increments on the clock face.
 * It is given the hour hand so it can be updated when the minute hand makes a
 * full rotation around the clock face.
 */
public class MinuteHand extends ClockHand {
    public final static double INCREMENT = Math.PI / 30.0;
    private static final int DEFAULT_MOVE_SPEED = 20;
    private final ClockHand hourHand;
    private final List<MinuteChangedListener> listeners = new LinkedList<MinuteChangedListener>();

    /**
     * Creates a new minute hand object.
     * @param x Pivot point x-coordinate.
     * @param y Pivot point y-coordinate.
     * @param value Initial hour value.
     * @param length Minute hand length.
     * @param p Paint to draw with.
     * @param grabPointSize Radius of the grab point in pixels.
     * @param parent Tha parent View.
     */
    public MinuteHand(float x, float y, int value, float length, Paint p,
                      float grabPointSize, View parent, ClockHand hourHand) {
        super(x, y, value, INCREMENT, length, p, grabPointSize, parent);
        this.hourHand = hourHand;
    }

    /**
     * Adds a MinuteChangedListener.
     * @param listener The listener to add.
     */
    public void addListener(MinuteChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets the current angle of the minute hand.
     * @return Current minute angle in radians.
     */
    @Override
    public double getAngle() {
        return (this.value * this.incrementSize);
    }

    /**
     * Sets the new minute value and informs listeners of change.
     * @param newValue New value for the clock hand.
     */
    @Override
    public void setValue(int newValue) {
        this.value = newValue;
        for (MinuteChangedListener mcl : listeners)
            mcl.onMinuteChanged(newValue);
    }

    /**
     * Sets the clock hand angle.
     * @param newAngle The new minute hand angle.
     */
    @Override
    public void setAngle(double newAngle) {
        double diff = angleDiff(getAngle(), newAngle);
        if (Math.abs(diff) > this.incrementSize) {
            double increments = (diff / this.incrementSize);
            increments = Math.round(increments);
            incrementValue((int) increments);
        }
    }

    /**
     * Increments the minute value.
     * @param inc Amount to increment the minute value by.
     */
    @Override
    public void incrementValue(int inc) {
        int val = getValue();
        val += inc;
        if (val >= 60) {
            val %= 60;
            hourHand.incrementValue(1);
        } else if (val < 0) {
            val += 60;
            hourHand.incrementValue(-1);
        }
        setValue(val);
    }

    /**
     * Creates an Animator object to move the hand to a target minute value.
     * @param target The target minute value to move to.
     * @return The created Animator object.
     */
    @Override
    protected Animator createAnimator(int target) {
        return new Animator(parent, DEFAULT_MOVE_SPEED, target, this) {
            @Override
            public void animate() {
                int minutes = movable.getValue();
                int dir = 1, inc = Math.abs(target - minutes);

                if (inc > 30) {
                    inc = Math.abs(inc - 60);
                    dir *= -1;
                }

                if (minutes > target) {
                    dir *= -1;
                }

                movable.incrementValue(dir * Math.min(inc, 3));
            }
        };
    }
}
