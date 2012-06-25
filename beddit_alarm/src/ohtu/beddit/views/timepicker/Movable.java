package ohtu.beddit.views.timepicker;

import android.view.View;

/**
 * An abstraction of a view component that can respond to click events on the
 * screen, can be dragged by the user, and can move to some target position
 * using an animator.
 */
abstract class Movable {

    final View parent;

    /**
     * Creates a new Movable object
     * @param parent Tha parent View of the Movable.
     */
    Movable(View parent) {
        this.parent = parent;
    }

    /**
     * Gets the Movable's current value.
     * @return The current value of the Movable.
     */
    public abstract int getValue();

    /**
     * Increments the value of the movable.
     * @param inc Amount to increment by.
     */
    protected abstract void incrementValue(int inc);

    /**
     * Grabs the Movable if click coordinates are on the grab point.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if coordinates are on the grab point.
     */
    public abstract boolean grab(float x, float y);

    /**
     * Releases the grab on the Movable.
     */
    public abstract void releaseGrab();

    /**
     * Checks if the Movable has been grabbed.
     * @return True if the Movable is grabbed.
     */
    public abstract boolean isGrabbed();

    /**
     * Starts a click action if a position that the Movable can travel to was clicked.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return True if a click was registered.
     */
    public abstract boolean click(float x, float y);

    /**
     * Checks if the Movable was clicked.
     * @return True if the Movable was clicked.
     */
    public abstract boolean wasClicked();

    /**
     * Resets the clicked status of the Movable to false.
     */
    public abstract void releaseClick();

    /**
     * Creates a target value for the Movable from click coordinates
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     * @return A target value based on the given point (x,y).
     */
    public abstract int createTargetFromClick(float x, float y);

    /**
     * Sets the position of the Movable based on screen click coordinates.
     * @param x Screen x-coordinate.
     * @param y Screen y-coordinate.
     */
    public abstract void updatePositionFromClick(float x, float y);

    private Thread t;
    private Animator a;

    /**
     * Starts animating the movable.
     * @param target Target value for the Movable to animate to.
     * @param l An AnimationFinishedListener.
     */
    public void startAnimation(int target, AnimationFinishedListener l) {
        if (a == null) {
            a = createAnimator(target);
            a.addAnimationFinishedListener(l);
        } else {
            a.setTarget(target);
        }

        if (t == null || !t.isAlive())
            (t = new Thread(a)).start();
    }

    /**
     * Creates an Animator object to move the Movable to the target value.
     * @param target The target value to move to.
     * @return The created Animator object.
     */
    protected abstract Animator createAnimator(int target);
}
