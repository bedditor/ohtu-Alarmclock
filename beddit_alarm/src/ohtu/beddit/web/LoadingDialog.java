package ohtu.beddit.web;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import ohtu.beddit.R;

/**
 *  This class represents the loading screen, which displays when fetching data from the internet.
 *  It's an ordinary dialog with a few tweaks on context and added Log messages to ease bug catching.
 */
public class LoadingDialog extends Dialog {

    private static final String TAG = "LoadingDialog";

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.loading_dialog);
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void show() {
        Log.v(TAG, "show");
        super.show();
    }

    @Override
    public void dismiss() {
        Log.v(TAG, "dismiss");
        super.dismiss();
    }


}
