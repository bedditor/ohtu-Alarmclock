package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class ClockHand extends Movable {
    protected float x;
    protected float y;
    protected float length;
    protected float grabPointOffset;
    protected Paint p;
    protected GrabPoint gp;
    protected double incrementSize;
    protected int value;

    public ClockHand(float x, float y,
                     int value, double incrementSize,
                     float length, Paint p, float grabPointOffset, float grabPointSize, View parent) {
        super(parent);
        this.x = x;
        this.y = y;
        Log.v("CLOCK HAND VALUE <-", "0");
        this.value = value;
        this.grabPointOffset = grabPointOffset;
        this.length = length;
        this.p = p;
        this.incrementSize = incrementSize;
        this.gp = new GrabPoint(x + (float)Math.cos(getAngle()- Math.PI / 2) * (length-grabPointOffset),
                                y + (float)Math.sin(getAngle()- Math.PI / 2) * (length-grabPointOffset),
                                grabPointSize, p);
    }

    public void draw(Canvas c) {
        double xDir = Math.cos(getAngle() - Math.PI / 2);
        double yDir = Math.sin(getAngle() - Math.PI / 2);

        float endX = (float) xDir * length;
        float endY = (float) yDir * length;

        gp.setX(x + (float) xDir * (length-grabPointOffset));
        gp.setY(y + (float) yDir * (length-grabPointOffset));

        gp.draw(c);
        c.drawLine(x, y, x + endX, y + endY, p);

        p.setStyle(Paint.Style.FILL);
        c.drawCircle(x,y,p.getStrokeWidth()/2,p);
        p.setStyle(Paint.Style.STROKE);
    }

    boolean grabbed = false;
    boolean clicked = false;

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
    public void releaseClick() {
        clicked = false;
    }

    @Override
    public boolean wasClicked() {
        return clicked;
    }

    @Override
    public boolean click(float x, float y) {
        return clicked = dist(x,y,this.x,this.y) < length;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void updatePositionFromClick(float x, float y) {
        setAngle(getAngleToMidpoint(x,y));
    }

    public float getLength() {
        return length;
    }

    public abstract double getAngle();

    public abstract void setAngle(double angle);

    public abstract void incrementValue(int increment);

    public void setValue(int newValue) {
        this.value = newValue;
    }

    public double getAngleToMidpoint(float x_, float y_) {
        double angle = Math.atan((y_ - y) / (x_ - x)) + Math.PI / 2;
        if (x_ - x < 0) angle += Math.PI;
        return angle;
    }

    public static double angleDiff(double a1, double a2) {
        double diff = a2 - a1;
        if (diff < -Math.PI) diff += Math.PI*2;
        else if (diff > Math.PI) diff -= Math.PI*2;
        return diff;
    }

    public static double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

    @Override
    public int createTargetFromClick(float x, float y) {
        return (int)(getAngleToMidpoint(x, y) / incrementSize);
    }
}