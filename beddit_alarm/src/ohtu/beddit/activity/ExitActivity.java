package ohtu.beddit.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Paul
 * Date: 6/14/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
