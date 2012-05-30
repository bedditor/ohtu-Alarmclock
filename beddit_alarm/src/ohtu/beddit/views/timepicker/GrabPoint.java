package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 29.5.2012
 * Time: 11:26
 * To change this template use File | Settings | File Templates.
 */
public class GrabPoint {
    private float x;
    private float y;
    private float radius;
    private Paint p;

    public GrabPoint(float x, float y, float radius, Paint p)  {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.p = p;
    }

    public void draw(Canvas c) {
        c.drawCircle(x, y, radius, p);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean onGrabPoint(float x_, float y_) {
        return dist(x_,y_,x,y) < radius*2;
    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

}
