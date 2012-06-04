package ohtu.beddit.views.timepicker;

import android.util.Log;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 30.5.2012
 * Time: 9:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class Movable {

    protected View parent;

    public Movable(View parent) {
        this.parent = parent;
    }

    public abstract int getValue();
    public abstract void incrementValue(int inc);

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

    public boolean isAnimating() {
        return t != null && t.isAlive();
    }

    protected abstract Animator createAnimator(int target);
}
