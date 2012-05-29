package ohtu.beddit.views.timepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 28.5.2012
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class Slider {
    public float x;
    public float y;
    public float w;
    public float h;
    public int maxValue;
    public int currentValue;

    public float strokeWidth;
    public float grabPointRadius;

    public float sliderGrabX;
    public float sliderGrabY;

    public RectF getRectF() {
        return new RectF(x,y,x+w,y+h);
    }

    public void update(float newX) {
        if (newX < x)
            currentValue = 0;
        else if (newX > x+w)
            currentValue = maxValue;
        else
            currentValue = (int)(((newX - x)/w)*(float)maxValue);
    }

    public void draw(Canvas c) {
        Paint intervalBarPaint = new Paint();
        intervalBarPaint.setColor(Color.BLACK);

        intervalBarPaint.setStyle(Paint.Style.STROKE);
        intervalBarPaint.setStrokeWidth(strokeWidth);
        intervalBarPaint.setAntiAlias(true);

        c.drawLine(getRectF().left,
                getRectF().centerY(),
                getRectF().right,
                getRectF().centerY(),
                intervalBarPaint);

        sliderGrabX = getRectF().left + ((float)currentValue)/((float)maxValue) * w;
        sliderGrabY = getRectF().centerY();

        c.drawCircle(sliderGrabX, sliderGrabY, grabPointRadius, intervalBarPaint);
    }

    public boolean onGrabPoint(float x_, float y_) {
        Log.v("slider_grab_dist", ""+dist(x_,y_,sliderGrabX,sliderGrabY));
        Log.v("slider_grab_dist", ""+grabPointRadius*2);
        Log.v("slider_grab_dist", ""+(dist(x_,y_,sliderGrabX,sliderGrabY) < grabPointRadius*2));
        return dist(x_,y_,sliderGrabX,sliderGrabY) < grabPointRadius*2;

    }

    private double dist(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}
