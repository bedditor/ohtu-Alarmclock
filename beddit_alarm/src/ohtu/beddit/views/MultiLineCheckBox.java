package ohtu.beddit.views;


import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MultiLineCheckBox extends CheckBoxPreference {
    public MultiLineCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
        makeMultiLine(view);
    }

    protected void makeMultiLine(View view) {
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
