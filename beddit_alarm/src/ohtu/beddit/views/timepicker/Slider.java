package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class Slider extends Movable {

    private static final int DEFAULT_MOVE_SPEED = 5;

    private float x;
    private float y;
    private float w;
    private float h;
    private int maxValue;
    private int currentValue;

    private Paint p;
    private GrabPoint gp;



    public Slider(float x, float y, float width, float height,
                  int maxValue, int initValue, Paint p,
                  float grabPointSize, View parent) {
        super(parent);
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.maxValue = maxValue;
        this.currentValue = initValue;
        this.p = p;
        this.gp = new GrabPoint(getRectF().left + ((float)currentValue)/((float)maxValue) * w,
                                getRectF().centerY(), grabPointSize, p);
    }

    List<ValueChangedListener> listeners = new LinkedList<ValueChangedListener>();

    public void addListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    public RectF getRectF() {
        return new RectF(x,y,x+w,y+h);
    }

    public void draw(Canvas c) {
        c.drawLine(getRectF().left,
                getRectF().centerY(),
                getRectF().right,
                getRectF().centerY(),
                p);

        gp.setX(getRectF().left + ((float)currentValue)/((float)maxValue) * w);
        gp.setY(getRectF().centerY());
        gp.draw(c);
    }


    private boolean grabbed;
    private boolean clicked;

    @Override
    public boolean grab(float x, float y) {
        return grabbed = gp.onGrabPoint(x, y);
    }

    @Override
    public void releaseGrab() {
        grabbed = false;
    }

    @Override
    public boolean isGrabbed() {
        return grabbed;
    }

    @Override
    public int getValue() {
        return currentValue;
    }

    @Override
    public void incrementValue(int inc) {
        setValue(currentValue + inc);
    }

    @Override
    public boolean click(float x, float y) {
        return clicked = getRectF().contains(x,y);
    }

    @Override
    public boolean wasClicked() {
        return clicked;
    }

    @Override
    public void releaseClick() {
        clicked = false;
    }

    @Override
    public void updatePositionFromClick(float newX, float newY) {
        int newValue;
        if (newX < x)
            newValue = 0;
        else if (newX > x+w)
            newValue = maxValue;
        else
            newValue = (int)(((newX - x)/w)*(float)maxValue);
        setValue(newValue);
    }

    public void setValue(int newValue) {
        this.currentValue = newValue;
        for(ValueChangedListener sl : listeners) sl.onValueChanged(currentValue);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public int createTargetFromClick(float x, float y) {
        return (int) (((Math.min(getRectF().right,
                Math.max(getRectF().left, x)) - getRectF().left) /
                getRectF().width()) * (float) maxValue);
    }

    @Override
    protected Animator createAnimator(int target) {
        return new Animator(parent, DEFAULT_MOVE_SPEED, target, this) {
            @Override
            public void animate() {
                int interval = movable.getValue();
                if (interval < target)
                    movable.incrementValue(1);
                else
                    movable.incrementValue(-1);
            }
        };
    }
}
