package ohtu.beddit.views.timepicker;

import android.view.View;
import java.util.LinkedList;
import java.util.List;

/*
 * Used for animating the clock hands and interval slider when they are clicked.
 *
 */
public abstract class Animator implements Runnable {
    private final View parent;
    private final int sleepTime;
    int target;
    final Movable movable;
    private final List<AnimationFinishedListener> animationFinishedListeners = new LinkedList<AnimationFinishedListener>();

    public Animator(View parent, int sleepTime, int target, Movable movable) {
        this.parent = parent;
        this.sleepTime = sleepTime;
        this.target = target;
        this.movable = movable;
    }

    public void addAnimationFinishedListener(AnimationFinishedListener l) {
        animationFinishedListeners.add(l);
    }

    protected abstract void animate();

    public boolean notFinished() {
        return movable.getValue() != getTarget();
    }

    public void run() {
        while (notFinished()) {
            animate();

            parent.post(new Runnable() {
                public void run() {
                    parent.invalidate();
                }
            });

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {}
        }

        for (AnimationFinishedListener l : animationFinishedListeners)
            l.onAnimationFinished();
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
