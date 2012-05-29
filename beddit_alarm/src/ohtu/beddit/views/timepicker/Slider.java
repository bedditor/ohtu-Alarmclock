package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class Slider implements Grabbable {
    private float x;
    private float y;
    private float w;
    private float h;
    private int maxValue;
    private int currentValue;

    private Paint p;
    private GrabPoint gp;

    List<SliderListener> listeners = new LinkedList<SliderListener>();

    public Slider(float x, float y, float width, float height,
                  int maxValue, int initValue,
                  Paint p,
                  float grabPointSize) {
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

    public void addListener(SliderListener listener) {
        listeners.add(listener);
    }

    public RectF getRectF() {
        return new RectF(x,y,x+w,y+h);
    }

    public void update(float newX) {
        if (newX < x)
            currentValue = 0;
        else if (newX > x+w)
            currentValue = maxValue;
        else
            currentValue = (int)(((newX - x)/w)*(float)maxValue);

        for(SliderListener sl : listeners) sl.onValueChanged(currentValue);
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

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    private boolean grabbed;

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

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
}
