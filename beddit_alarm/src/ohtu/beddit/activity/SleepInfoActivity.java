package ohtu.beddit.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ohtu.beddit.R;
import org.w3c.dom.Text;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 6/11/12
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class SleepInfoActivity extends Activity {

    private Button feelGoodMan;
    private Button feelBatMan;
    private TextView overallSleep;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_info);

        feelGoodMan = (Button)findViewById(R.id.SleptWellButton);
        feelGoodMan.setOnClickListener(new SleepInfoActivity.FeelsGoodButtonClickListener());
        feelBatMan = (Button) findViewById(R.id.SleptBadlyButton);
        feelBatMan.setOnClickListener(new SleepInfoActivity.FeelsBadManButtonClickListener());

        overallSleep = (TextView)findViewById(R.id.sleep_info_overall_text);
        overallSleep.setText("TEST");

    }

    public class FeelsBadManButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //asd
        }
    }

    public class FeelsGoodButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //sad
        }
    }
}
