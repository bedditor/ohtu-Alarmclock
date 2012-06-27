package ohtu.beddit.activity;

import android.app.Activity;
import android.os.Bundle;
import ohtu.beddit.R;


/**
 * Help activity is created when the "Help" button is pressed in the application menu.
 * Help text is defined in the help.xml file.
 */


public class HelpActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }
}
