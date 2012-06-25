package ohtu.beddit.views.timepicker;

import android.view.View;
import java.util.LinkedList;
import java.util.List;

/**
 * Used for animating the clock hands and interval slider when they are clicked.
 */
public abstract class Animator implements Runnable {
    private final View parent;
    private final int sleepTime;
    int target;
    final Movable movable;
    private final List<AnimationFinishedListener> animationFinishedListeners = new LinkedList<AnimationFinishedListener>();

    /**
     * Creates an Animator object for the chosen Movable.
     * @param parent The parent View of the Movable to animate.
     * @param sleepTime Time in ms to sleep between animation ticks.
     * @param target The target value for the Movable.
     * @param movable The Movable to animate.
     */
    public Animator(View parent, int sleepTime, int target, Movable movable) {
        this.parent = parent;
        this.sleepTime = sleepTime;
        this.target = target;
        this.movable = movable;
    }

    /**
     * Adds a new AnimationFinishedListener object
     * @param l The listener to add.
     */
    public void addAnimationFinishedListener(AnimationFinishedListener l) {
        animationFinishedListeners.add(l);
    }

    /**
     * Performs one tick of the animation.
     */
    protected abstract void animate();

    /**
     * Checks if the animation has finished.
     * @return false when the animation is finished.
     */
    public boolean notFinished() {
        return movable.getValue() != getTarget();
    }

    /**
     * Runs the animation.
     */
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

    /**
     * Returns the target value for the animation.
     * @return Animation target.
     */
    public int getTarget() {
        return target;
    }

    /**
     * Sets the target value for the animation.
     * @param target New animation target.
     */
    public void setTarget(int target) {
        this.target = target;
    }
}
