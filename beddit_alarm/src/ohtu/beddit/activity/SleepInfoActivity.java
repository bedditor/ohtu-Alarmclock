package ohtu.beddit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.JsonParser;
import ohtu.beddit.R;
import ohtu.beddit.utils.Utils;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

import java.util.Calendar;

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
    private String nightInfo;
    private final String TAG = "SleepInfoActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_info);

        if (getNightInfo()){
            setButtons();
            updateText();
        } else {
            this.finish();
        }


    }

    private boolean getNightInfo() {
        BedditWebConnector connectori = new BedditWebConnector();
        nightInfo = "";
        String date = Utils.getTodayAsQueryDateString();
        try {
            nightInfo = connectori.getWakeUpJson(this, date);
            return true;
        } catch (MalformedBedditJsonException e) {
            Log.v(TAG, "failed to get wake up info");
            e.printStackTrace();
            return false;
        }
    }

    private void updateText() {
        ((TextView) findViewById(R.id.sleep_info_overall_text)).setText(getHoursAndMinutesFromSeconds(getValueOfKeyFromJson(nightInfo, "time_sleeping")));
        ((TextView) findViewById(R.id.sleep_info_deep_text)).setText(getHoursAndMinutesFromSeconds(getValueOfKeyFromJson(nightInfo, "time_deep_sleep")));

        String dataDate = getValueOfKeyFromJson(nightInfo, "local_analyzed_up_to_time");

        Log.v(TAG, "Local analyzed up to time: " + getValueOfKeyFromJson(nightInfo, "local_analyzed_up_to_time") + ", Device time is (LocaleString (+3gmt)) " + Calendar.getInstance().getTime().toLocaleString());
        if (getValueOfKeyFromJson(nightInfo, "is_analysis_up_to_date").equalsIgnoreCase("true"))
            ((TextView) findViewById(R.id.sleep_info_delay)).setText("Data is up to date");
        else
            ((TextView) findViewById(R.id.sleep_info_delay)).setText(getTimeDifference(dataDate));
    }

    private void setButtons() {
        feelGoodMan = (Button) findViewById(R.id.SleptWellButton);
        feelGoodMan.setOnClickListener(new SleepInfoActivity.FeelsGoodButtonClickListener());
        feelBatMan = (Button) findViewById(R.id.SleptBadlyButton);
        feelBatMan.setOnClickListener(new SleepInfoActivity.FeelsBadManButtonClickListener());
    }


    public class FeelsBadManButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    public class FeelsGoodButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }


    private String getValueOfKeyFromJson(String json, String key) {
        return new JsonParser().parse(json).getAsJsonObject().get(key).getAsString();
    }

    private String getHoursAndMinutesFromSeconds(String rawdata) {
        int lol = Integer.parseInt(rawdata);
        return lol / 3600 + "h " + (lol / 60) % 60 + "min";
    }

    //expects format like this 2012-06-13T08:38:11 Please don't break it :)
    private String getTimeDifference(String data) {
        String parsed = data.substring(11);
        int hours = Integer.parseInt(parsed.substring(0, 2));
        int minutes = Integer.parseInt(parsed.substring(3, 5));
        int diffhours = Calendar.getInstance().getTime().getHours() - hours;
        int diffminutes = Calendar.getInstance().getTime().getMinutes() - minutes;
        if (diffminutes < 0) {
            diffhours -= 1;
            diffminutes = 60+diffminutes;
        }
        if (diffhours < 0) {
            Log.v("fazias", data + " is odd compared to " + Calendar.getInstance().getTime().toLocaleString() +
                    "\nhours = " + hours + ", minutes = " + minutes + ", diffhours = " + diffhours + ", diffminutes = " +diffminutes);
            return "(error 101) How did you get in the future!?";
        }
        return "Data is " +diffhours + "h " + diffminutes + "min old.";
    }
}
