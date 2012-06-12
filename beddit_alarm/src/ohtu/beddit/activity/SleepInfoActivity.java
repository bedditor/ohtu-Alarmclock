package ohtu.beddit.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.GpsStatus;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.JsonParser;
import ohtu.beddit.R;
import ohtu.beddit.alarm.AlarmCheckerRealImpl;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.MalformedBedditJsonException;
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

        BedditWebConnector connectori = new BedditWebConnector();
        String nightInfo = "";
        String date = AlarmCheckerRealImpl.getQueryDateString();
        try {
            nightInfo = connectori.getWakeUpJson(this, date);
        } catch (MalformedBedditJsonException e) {
            e.printStackTrace();
        }

        feelGoodMan = (Button)findViewById(R.id.SleptWellButton);
        feelGoodMan.setOnClickListener(new SleepInfoActivity.FeelsGoodButtonClickListener());
        feelBatMan = (Button) findViewById(R.id.SleptBadlyButton);
        feelBatMan.setOnClickListener(new SleepInfoActivity.FeelsBadManButtonClickListener());

        ((TextView)findViewById(R.id.sleep_info_overall_text)).setText(getHoursAndMinutesFromSeconds(getValueOfKeyFromJson(nightInfo, "time_sleeping")));
        //((TextView)findViewById(R.id.sleep_info_overall_minitext)).setText(getValueOfKeyFromJson(nightInfo, "time_sleeping"));
        ((TextView)findViewById(R.id.sleep_info_deep_text)).setText(getHoursAndMinutesFromSeconds(getValueOfKeyFromJson(nightInfo, "time_deep_sleep")));
        //((TextView)findViewById(R.id.sleep_info_deep_minitext)).setText(getValueOfKeyFromJson(nightInfo, "time_sleeping"));

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


    private String getValueOfKeyFromJson(String json, String key)
    {
        return new JsonParser().parse(json).getAsJsonObject().get(key).getAsString();
    }
    private String getHoursAndMinutesFromSeconds(String rawdata) {
        int lol = Integer.parseInt(rawdata);
        return lol/3600 + "h " + (lol/60)%60 + "min";
    }
}
