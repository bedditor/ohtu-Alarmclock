package ohtu.beddit.views.timepicker;

import android.graphics.Paint;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 30.5.2012
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class MinuteHand extends ClockHand {

    private static final int DEFAULT_MOVE_SPEED = 5;
    private ClockHand hourHand;

    public MinuteHand(float x, float y, int value, double incrementSize, float length, Paint p,
                      float grabPointOffset, float grabPointSize, View parent, ClockHand hourHand) {
        super(x, y, value, incrementSize, length, p, grabPointOffset, grabPointSize, parent);
        this.hourHand = hourHand;
    }

    List<MinuteChangedListener> listeners = new LinkedList<MinuteChangedListener>();

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
            incrementValue((int)increments);
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
                if (minutes < target)  {
                    if (Math.abs(minutes - target) > 30)
                        movable.incrementValue(-1);
                    else
                        movable.incrementValue(1);
                } else {
                    if (Math.abs(minutes - target) > 30)
                        movable.incrementValue(1);
                    else
                        movable.incrementValue(-1);
                }
            }
        };
    }
}
