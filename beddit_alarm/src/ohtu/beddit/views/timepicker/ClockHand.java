package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class ClockHand {
    public float x;
    public float y;
    public double angle;
    public float length;
    public float grabPointSize;
    public float grabPointX;
    public float grabPointY;
    public float strokeWidth;
    public float grabPointOffset;

    public void draw(Canvas c) {
        Paint clockHand = new Paint();
        clockHand.setColor(Color.BLACK);
        clockHand.setAntiAlias(true);
        clockHand.setStrokeWidth(strokeWidth);
        clockHand.setStyle(Paint.Style.STROKE);

        double xDir = Math.cos(angle);
        double yDir = Math.sin(angle);

        float endX = (float) xDir * length;
        float endY = (float) yDir * length;

        grabPointX = x + (float) xDir * (length-grabPointOffset);
        grabPointY = y + (float) yDir * (length-grabPointOffset);

        c.drawLine(x, y, x + endX, y + endY, clockHand);

        c.drawCircle(grabPointX, grabPointY, grabPointSize, clockHand);
        clockHand.setStyle(Paint.Style.FILL);
        c.drawCircle(x,y,strokeWidth/2,clockHand);
    }

    public boolean onGrabPoint(float x_, float y_) {
        return dist(x_,y_,grabPointX,grabPointY) < grabPointSize*2;
    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }

}
