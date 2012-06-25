package ohtu.beddit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView for showing icons that keeps its aspect ratio
 * at a constant 1:1.
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minDimension = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(minDimension, minDimension);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
