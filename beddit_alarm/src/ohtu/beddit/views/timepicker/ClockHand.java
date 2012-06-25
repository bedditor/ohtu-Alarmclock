package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * ClockHand defines how to draw and general functionality for clock hands
 */
public abstract class ClockHand extends Movable {
    private final float x;
    private final float y;
    private final float length;
    private final Paint p;
    private final GrabPoint gp;
    final double incrementSize;
    int value;

    /**
     * Creates a new ClockHand object
     * @param x The center x-coordinate.
     * @param y The center y-coordinate.
     * @param value Initial value for the clock hand.
     * @param incrementSize Amount in radians to move the hand by per tick.
     * @param length The length in pixels of the clock hand.
     * @param p Paint to use for drawing the hand.
     * @param grabPointSize The radius in pixels of the clock hand grab point.
     * @param parent The view that the clock hand is connected to.
     */
    ClockHand(float x, float y,
              int value, double incrementSize,
              float length, Paint p, float grabPointSize, View parent) {
        super(parent);
        this.x = x;
        this.y = y;
        this.value = value;
        this.length = length;
        this.p = p;
        this.incrementSize = incrementSize;
        this.gp = new GrabPoint(x + (float) Math.cos(getAngle() - Math.PI / 2) * (length - 2 * grabPointSize),
                y + (float) Math.sin(getAngle() - Math.PI / 2) * (length - 2 * grabPointSize),
                grabPointSize, p);
    }

    /**
     * Draws the clock hand on the given Canvas.
     * @param c The Canvas object to draw to.
     */
    public void draw(Canvas c) {
        double xDir = Math.cos(getAngle() - Math.PI / 2);
        double yDir = Math.sin(getAngle() - Math.PI / 2);

        float endX = (float) xDir * length;
        float endY = (float) yDir * length;

        gp.setX(x + (float) xDir * (length - 2 * gp.getRadius()));
        gp.setY(y + (float) yDir * (length - 2 * gp.getRadius()));

        gp.draw(c);
        c.drawLine(x, y, x + endX, y + endY, p);

        p.setStyle(Paint.Style.FILL);
        c.drawCircle(x, y, p.getStrokeWidth() / 2, p);
        p.setStyle(Paint.Style.STROKE);
    }

    private boolean grabbed = false;
    private boolean clicked = false;

    /**
     * Grabs the clock hand if click coordinates are on the grab point.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if coordinated are on the grab point.
     */
    @Override
    public boolean grab(float x, float y) {
        return grabbed = gp.onGrabPoint(x, y);
    }

    /**
     * Releases the grab on the clock hand.
     */
    @Override
    public void releaseGrab() {
        grabbed = false;
    }

    /**
     * Checks if the clock hand has been grabbed.
     * @return True if the clock hand is grabbed.
     */
    @Override
    public boolean isGrabbed() {
        return grabbed;
    }

    /**
     * Starts a click action if a position that the clock hand can travel to
     * was clicked.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if a click was registered.
     */
    @Override
    public boolean click(float x, float y) {
        double distanceFromCenter = dist(x, y, this.x, this.y);
        return clicked = distanceFromCenter < length && distanceFromCenter > length - 4 * gp.getRadius();
    }

    /**
     * Resets the clicked status of the clock hand to false.
     */
    @Override
    public void releaseClick() {
        clicked = false;
    }

    /**
     * Checks if the clock hand was clicked.
     * @return True if the clock hand was clicked.
     */
    @Override
    public boolean wasClicked() {
        return clicked;
    }

    /**
     * Gets the value of the clock hand.
     * @return Clock hand value.
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the angle of the clock hand based on screen click coordinates.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     */
    @Override
    public void updatePositionFromClick(float x, float y) {
        setAngle(getAngleToMidpoint(x, y));
    }

    /**
     * Gets the length of the clock hand.
     * @return Clock hand length.
     */
    public float getLength() {
        return length;
    }

    /**
     * Gets the currrent angle of the clock hand.
     * @return Clock hand angle.
     */
    public abstract double getAngle();

    /**
     * Sets the angle of the clock hand.
     * @param angle New clock hand angle.
     */
    protected abstract void setAngle(double angle);

    /**
     * Increments the value of the clock hand.
     * @param increment Amount to increment the value by.
     */
    public abstract void incrementValue(int increment);

    /**
     * Sets the value of the clock hand.
     * @param newValue New value for the clock hand.
     */
    public void setValue(int newValue) {
        this.value = newValue;
    }

    /**
     * Gets the angle to the point the clock hand rotates around of the given screen coordinates.
     * @param x_ Screen x-coordinate.
     * @param y_ Screen y-coordinate.
     * @return The angle to midpoint.
     */
    double getAngleToMidpoint(float x_, float y_) {
        double angle = Math.atan((y_ - y) / (x_ - x)) + Math.PI / 2;
        if (x_ - x < 0) angle += Math.PI;
        return angle;
    }

    /**
     * General utility method for getting the difference between two angles.
     * @param a1 The first angle in radians.
     * @param a2 The second angle in radians.
     * @return The angle between a1 and a2 in radians.
     */
    static double angleDiff(double a1, double a2) {
        double diff = a2 - a1;
        if (diff < -Math.PI) diff += Math.PI * 2;
        else if (diff > Math.PI) diff -= Math.PI * 2;
        return diff;
    }

    /**
     * General utility method for getting the distance between two points
     * @param x1 The first x-coordinate.
     * @param y1 The first y-coordinate.
     * @param x2 The second x-coordinate.
     * @param y2 The second y-coordinate.
     * @return The distance between (x1,y1) and (x2,y2).
     */
    private static double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * Creates a target value for the clock hand from click coordinates
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return A target value based on the given point (x,y).
     */
    @Override
    public int createTargetFromClick(float x, float y) {
        return (int) (getAngleToMidpoint(x, y) / incrementSize);
    }
}