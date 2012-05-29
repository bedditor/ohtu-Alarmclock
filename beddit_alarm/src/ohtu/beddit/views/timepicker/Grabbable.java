package ohtu.beddit.views.timepicker;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 29.5.2012
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public interface Grabbable {
    public boolean grab(float x, float y);
    public void releaseGrab();
    public boolean isGrabbed();
}
