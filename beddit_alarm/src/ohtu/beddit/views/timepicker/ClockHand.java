package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class ClockHand implements Grabbable {
    private float x;
    private float y;
    private double angle;
    private float length;
    private float grabPointOffset;
    private Paint p;
    private GrabPoint gp;

    public ClockHand(float x, float y, double angle, float length, Paint p, float grabPointOffset, float grabPointSize) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.grabPointOffset = grabPointOffset;
        this.length = length;
        this.p = p;
        this.gp = new GrabPoint(x + (float)Math.cos(angle) * (length-grabPointOffset),
                                y + (float)Math.sin(angle) * (length-grabPointOffset),
                                grabPointSize, p);
    }

    public void draw(Canvas c) {
        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);

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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public double getAngleToMidpoint(float x_, float y_) {
        double angle = Math.atan((y_ - y) / (x_ - x)) + Math.PI / 2;
        if (x_ - x < 0) angle += Math.PI;
        return angle;
    }
}
