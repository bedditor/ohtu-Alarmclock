package ohtu.beddit.views.timepicker;

import android.view.View;

/**
 * An abstraction of a view component that can respond to click events on the
 * screen, can be dragged by the user, and can move to some target position
 * using an animator.
 */
abstract class Movable {

    final View parent;

    Movable(View parent) {
        this.parent = parent;
    }

    public abstract int getValue();

    protected abstract void incrementValue(int inc);

    public abstract boolean grab(float x, float y);

    public abstract void releaseGrab();

    public abstract boolean isGrabbed();

    public abstract boolean click(float x, float y);

    public abstract boolean wasClicked();

    public abstract void releaseClick();

    public abstract int createTargetFromClick(float x, float y);

    public abstract void updatePositionFromClick(float x, float y);

    private Thread t;
    private Animator a;

    public void animate(int target, AnimationFinishedListener l) {
        if (a == null) {
            a = createAnimator(target);
            a.addAnimationFinishedListener(l);
        } else {
            a.setTarget(target);
        }

        if (t == null || !t.isAlive())
            (t = new Thread(a)).start();
    }

    protected abstract Animator createAnimator(int target);
}
