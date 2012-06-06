package ohtu.beddit;


import android.graphics.Canvas;
import android.graphics.Paint;
import ohtu.beddit.views.timepicker.GrabPoint;
import org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: tahuomo
 * Date: 6.6.2012
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
public class GrabPointTest {
    private GrabPoint grapper;


    @Test
    public void testDraw() {
        Paint p = mock(Paint.class);
        Canvas c = mock(Canvas.class);
        grapper = new GrabPoint(5, 4, 5, p);
        grapper.draw(c);

        verify(c).drawCircle(5, 4, 5, p);
    }

    @Test
   public void testOnGrabPoint(){

    }

    @Test
    public void notOnGrabPoint(){

    }


  /*  public boolean onGrabPoint(float x_, float y_) {
        return dist(x_,y_,x,y) < radius*2;
    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }*/

}
