package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * A slider that can be manipulated by the user
 */
public class Slider extends Movable {

    private static final int DEFAULT_MOVE_SPEED = 5;

    private final float x;
    private final float y;
    private final float w;
    private final float h;
    private final int maxValue;
    private int currentValue;

    private final Paint p;
    private final GrabPoint gp;

    private final List<ValueChangedListener> listeners = new LinkedList<ValueChangedListener>();

    /**
     * Creates a new slider object
     * @param x Top left x-coordinate.
     * @param y Top left y-coordinate.
     * @param width Width of the slider in pixels.
     * @param height Height of the bounding box of the slider.
     * @param maxValue Maximum value for the slider.
     * @param initValue Initial value for the slider.
     * @param p Paint to draw the slider with.
     * @param grabPointSize Size of the slider grab point.
     * @param parent Parent view of the slider.
     */
    public Slider(float x, float y, float width, float height,
                  int maxValue, int initValue, Paint p,
                  float grabPointSize, View parent) {
        super(parent);
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.maxValue = maxValue;
        setValue(initValue);
        this.p = p;
        this.gp = new GrabPoint(getRectF().left + ((float) currentValue) / ((float) maxValue) * w,
                getRectF().centerY(), grabPointSize, p);
    }

    /**
     * Adds a ValueChangedListener.
     * @param listener The listener to add.
     */
    public void addListener(ValueChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * Gets the bounding rectangle for the slider.
     * @return Bounding rectangle.
     */
    RectF getRectF() {
        return new RectF(x, y, x + w, y + h);
    }

    /**
     * Draws the slider.
     * @param c The Canvas object to draw to.
     */
    public void draw(Canvas c) {
        c.drawLine(getRectF().left,
                getRectF().centerY(),
                getRectF().right,
                getRectF().centerY(),
                p);

        gp.setX(getRectF().left + ((float) currentValue) / ((float) maxValue) * w);
        gp.setY(getRectF().centerY());
        gp.draw(c);
    }

    private boolean grabbed;
    private boolean clicked;

    /**
     * Grabs the slider if click coordinates are on the grab point.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if coordinates are on the grab point.
     */
    @Override
    public boolean grab(float x, float y) {
        return grabbed = gp.onGrabPoint(x, y);
    }

    /**
     * Releases the grab on the slider.
     */
    @Override
    public void releaseGrab() {
        grabbed = false;
    }

    /**
     * Checks if the slider has been grabbed.
     * @return True if the slider is grabbed.
     */
    @Override
    public boolean isGrabbed() {
        return grabbed;
    }

    /**
     * Gets the current value of the slider.
     * @return Current value.
     */
    @Override
    public int getValue() {
        return currentValue;
    }

    /**
     * Increments the slider's value.
     * @param inc Amount to increment by.
     */
    @Override
    public void incrementValue(int inc) {
        setValue(currentValue + inc);
    }

    /**
     * Starts a click action if a position that the slider can travel to
     * was clicked.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if a click was registered.
     */
    @Override
    public boolean click(float x, float y) {
        return clicked = getRectF().contains(x, y);
    }

    /**
     * Checks if the slider was clicked.
     * @return True if the slider was clicked.
     */
    @Override
    public boolean wasClicked() {
        return clicked;
    }

    /**
     * Resets the clicked status of the slider to false.
     */
    @Override
    public void releaseClick() {
        clicked = false;
    }

    /**
     * Sets the position of the slider based on screen click coordinates.
     * @param newX Screen x-coordinate.
     * @param newY Screen y-coordinate.
     */
    @Override
    public void updatePositionFromClick(float newX, float newY) {
        int newValue;
        if (newX < x)
            newValue = 0;
        else if (newX > x + w)
            newValue = maxValue;
        else
            newValue = (int) (((newX - x) / w) * (float) maxValue);
        setValue(newValue);
    }

    /**
     * Sets a new value for the slider.
     * @param newValue New slider value.
     */
    public void setValue(int newValue) {
        this.currentValue = Math.min(newValue, maxValue);
        for (ValueChangedListener sl : listeners) sl.onValueChanged(currentValue);
    }

    /**
     * Creates a target value for the slider from click coordinates
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return A target value based on the given point (x,y).
     */
    @Override
    public int createTargetFromClick(float x, float y) {
        return (int) (((Math.min(getRectF().right,
                Math.max(getRectF().left, x)) - getRectF().left) /
                getRectF().width()) * (float) maxValue);
    }

    /**
     * Creates an Animator object to move the slider to the target value.
     * @param target The target value to move to.
     * @return The created Animator object.
     */
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
