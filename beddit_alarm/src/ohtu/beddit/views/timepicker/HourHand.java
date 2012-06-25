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

    public HourHand(float x, float y, int value, double incrementSize, float length, Paint p,
                    float grabPointSize, View parent) {
        super(x, y, value, incrementSize, length, p, grabPointSize, parent);
    }


    public void addListener(HourChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onMinuteChanged(int value) {
        this.minuteValue = value;
    }

    @Override
    public void setValue(int newValue) {
        this.value = newValue;
        for (HourChangedListener hcl : listeners)
            hcl.onHourChanged(newValue);
    }

    @Override
    public double getAngle() {
        return ((this.value % 12) * this.incrementSize) +
                (minuteValue / 60.0 * this.incrementSize);
    }

    @Override
    public void incrementValue(int inc) {
        int val = getValue();
        val = (val + inc) % 24;
        if (val < 0) val += 24;
        setValue(val);
    }

    @Override
    public void setAngle(double newAngle) {

        double diff = ClockHand.angleDiff(getAngle(), newAngle);
        if (Math.abs(diff) > this.incrementSize) {
            double increments = (diff / this.incrementSize);
            increments = Math.round(increments);
            incrementValue((int) increments);
        }
    }

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