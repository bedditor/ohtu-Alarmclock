package ohtu.beddit.web;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import ohtu.beddit.R;

/**
 * Created with IntelliJ IDEA.
 * User: Paul
 * Date: 6/18/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadingDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    public LoadingDialog(Context context) {
        super(context);
        setContentView(R.layout.loading_dialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.loading_dialog);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void show() {
        Log.v(TAG, "show");
        super.show();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void dismiss() {
        Log.v(TAG, "dismiss");
        super.dismiss();    //To change body of overridden methods use File | Settings | File Templates.
    }


}
