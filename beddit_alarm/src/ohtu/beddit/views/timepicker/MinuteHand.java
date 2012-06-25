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

    private static final int DEFAULT_MOVE_SPEED = 20;
    private final ClockHand hourHand;

    public MinuteHand(float x, float y, int value, double incrementSize, float length, Paint p,
                      float grabPointSize, View parent, ClockHand hourHand) {
        super(x, y, value, incrementSize, length, p, grabPointSize, parent);
        this.hourHand = hourHand;
    }

    private final List<MinuteChangedListener> listeners = new LinkedList<MinuteChangedListener>();

    public void addListener(MinuteChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public double getAngle() {
        return (this.value * this.incrementSize);
    }

    @Override
    public void setValue(int newValue) {
        this.value = newValue;
        for (MinuteChangedListener mcl : listeners)
            mcl.onMinuteChanged(newValue);
    }

    @Override
    public void setAngle(double newAngle) {
        double diff = angleDiff(getAngle(), newAngle);
        if (Math.abs(diff) > this.incrementSize) {
            double increments = (diff / this.incrementSize);
            increments = Math.round(increments);
            incrementValue((int) increments);
        }
    }

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
