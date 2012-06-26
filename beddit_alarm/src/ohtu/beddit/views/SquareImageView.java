package ohtu.beddit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView for showing icons that keeps its aspect ratio
 * at a constant 1:1.
 */
public class SquareImageView extends ImageView {

    /**
     * Creates a new SquareImageView with attributes from the xml.
     * @param context Automatically provided by Android.
     * @param attrs Set of attributes from xml.
     */
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Force the view to be square.
     * @param widthMeasureSpec Width measurement spec from Android system.
     * @param heightMeasureSpec Height measurement spec from Android system.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minDimension = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(minDimension, minDimension);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
