package ohtu.beddit.views.timepicker;

import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 29.5.2012
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
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

    public boolean running() {
        return movable.getValue() != target;
    }

    public void run() {
        while (running()) {
            animate();

            parent.post(new Runnable() {
                public void run() {
                    parent.invalidate();
                }
            });

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
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
