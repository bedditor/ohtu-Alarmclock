package ohtu.beddit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 13.6.2012
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class TextScalingButton extends Button {
    public TextScalingButton(Context context) {
        super(context);
    }

    public TextScalingButton(Context context, AttributeSet attrs) {
        super(context, attrs);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float textLen = getPaint().measureText(getText().toString());
        float scale = w / textLen * 0.8f; // scale text to fill 80% of button width
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * scale);
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
