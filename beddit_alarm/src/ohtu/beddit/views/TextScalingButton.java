package ohtu.beddit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import ohtu.beddit.R;

/**
 * Created with IntelliJ IDEA.
 * User: psaikko
 * Date: 13.6.2012
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
public class TextScalingButton extends Button {

    private String textScalingReference;

    public TextScalingButton(Context context) {
        super(context);
    }

    public TextScalingButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextScaling);


        textScalingReference = a.getString(
                R.styleable.TextScaling_text_scale_ref);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float textLen = getPaint().measureText(getText().toString());
        float scale = w / textLen * 0.8f; // scale text to fill 80% of button width

        if (textScalingReference == null) textScalingReference = getText().toString();
        float refTextLen = getPaint().measureText(textScalingReference);
        float refScale = w / refTextLen * 0.8f;

        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * Math.min(scale, refScale));
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
