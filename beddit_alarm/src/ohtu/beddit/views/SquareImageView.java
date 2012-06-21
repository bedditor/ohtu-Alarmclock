package ohtu.beddit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 21.6.2012
 * Time: 10:11
 * To change this template use File | Settings | File Templates.
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
