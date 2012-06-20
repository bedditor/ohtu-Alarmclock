package ohtu.beddit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 6/20/12
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScalingTextView extends TextView {
    public ScalingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ScalingTextView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float textLen = getPaint().measureText(getText().toString());
        float scale = w / textLen * 0.9f; // scale text to fill 80% of button width
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * scale);
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
