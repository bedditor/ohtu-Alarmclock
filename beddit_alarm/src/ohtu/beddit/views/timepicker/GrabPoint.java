package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Defines an area on the screen that the user can select and drag with a finger
 */
public class GrabPoint {
    private float x;
    private float y;
    private final float radius;
    private final Paint p;
    private static final int COLOR_ALPHA = 64;

    /**
     * Creates a grab point with the specified location and size.
     * @param x Center x-coordinate.
     * @param y Center y-coordinate.
     * @param radius Grab point radius.
     * @param p Paint to use for drawing the grab point.
     */
    public GrabPoint(float x, float y, float radius, Paint p) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.p = p;
    }

    /**
     * Draws the grab point with a slightly transparent version of the given color
     * @param c Canvas object to draw to.
     */
    public void draw(Canvas c) {
        int color = p.getColor();
        p.setColor(color - ((255 - COLOR_ALPHA) << 24));
        c.drawCircle(x, y, radius, p);
        p.setColor(color);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    /**
     * Checks if the specified coordinate is on the grab point.
     * @param x_ Screen x-coordinate.
     * @param y_ Screen y-coordinate.
     * @return True if (x_,y_) is on the grab point.
     */
    public boolean onGrabPoint(float x_, float y_) {
        return dist(x_, y_, x, y) < radius * 2;
    }

    /**
     * General utility method for getting the distance between two points
     * @param x1 The first x-coordinate.
     * @param y1 The first y-coordinate.
     * @param x2 The second x-coordinate.
     * @param y2 The second y-coordinate.
     * @return The distance between (x1,y1) and (x2,y2).
     */
    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

}
