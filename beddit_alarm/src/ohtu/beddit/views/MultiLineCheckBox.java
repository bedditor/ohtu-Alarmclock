package ohtu.beddit.views;


import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A checkbox with multi line text for the preference screen
 */
public class MultiLineCheckBox extends CheckBoxPreference {
    /**
     * Creates a new MultiLineCheckBox with attributes from the xml.
     * @param context Automatically provided by Android.
     * @param attrs Set of attributes from xml.
     */
    public MultiLineCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        makeMultiLine(view);
    }

    /**
     * Make the view and its components multi line
     * @param view The view to modify.
     */
    void makeMultiLine(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup grp = (ViewGroup) view;
            for (int index = 0; index < grp.getChildCount(); index++) {
                makeMultiLine(grp.getChildAt(index));
            }
        } else if (view instanceof TextView) {
            TextView t = (TextView) view;
            t.setSingleLine(false);
            t.setEllipsize(null);
        }
    }

}
