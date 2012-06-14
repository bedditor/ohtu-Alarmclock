package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import ohtu.beddit.R;

public class HelpActivity extends Activity {

    private static final String TAG = "HelpActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    @Override
    public void onAttachedToWindow() {
        Log.v(TAG, "SETTING KEYGUARD ON");
        Log.v(TAG, "onAttachedToWindow");
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Log.v(TAG, "HOME PRESSED");
            setResult(MainActivity.RESULT_HOME_BUTTON_KILL);
            finish();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.v(TAG, "BACK PRESSED");
        }

        if (keyCode == KeyEvent.KEYCODE_CALL) {
            Log.v(TAG, "CALL PRESSED");
            setResult(MainActivity.RESULT_CALL_BUTTON_KILL);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
