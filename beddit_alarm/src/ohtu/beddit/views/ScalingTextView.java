package ohtu.beddit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import ohtu.beddit.R;

/**
 * A TextView that scales its text to fill itself horizontally.
 * If given a textScalingReference it will use a text size that
 * would fit the longer of the two strings for its text.
 */
public class ScalingTextView extends TextView {
    private String textScalingReference;
    private float parentWidthPercent = 1.0f;

    /**
     * Creates a new ScalingTextView with attributes from the xml.
     * @param context Automatically provided by Android.
     * @param attrs Set of attributes from xml.
     */
    public ScalingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.TextScaling);

        textScalingReference = a.getString(R.styleable.TextScaling_text_scale_ref);
        parentWidthPercent = a.getFloat(R.styleable.TextScaling_parent_width_percent, parentWidthPercent);
        a.recycle();
    }

    /**
     * Scale text based on the View's width and a reference string if one exists.
     * @param w New view width.
     * @param h New view height.
     * @param oldw Old width. Not used.
     * @param oldh Old height. Not used.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float textLen = getPaint().measureText(getText().toString());
        float scale = w / textLen * parentWidthPercent; // scale text to fill 90% of text view width

        if (textScalingReference == null) textScalingReference = getText().toString();
        float refTextLen = getPaint().measureText(textScalingReference);
        float refScale = w / refTextLen * parentWidthPercent;

        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize() * Math.min(scale, refScale));
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
