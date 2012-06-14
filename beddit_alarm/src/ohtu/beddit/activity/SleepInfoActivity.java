package ohtu.beddit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import ohtu.beddit.R;
import ohtu.beddit.utils.Utils;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.utils.Utils;
import ohtu.beddit.web.BedditConnectionException;

import java.util.Calendar;


public class SleepInfoActivity extends Activity {

    private Button feelGoodMan;
    private Button feelBatMan;
    private int timeSleeping;
    private int timeDeepSleep;
    private String localAnalyzedUpToTime;
    private boolean isAnalysisUpToDate;
    private final String TAG = "SleepInfoActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_info);

        try {
            updateNightInfo();
            setButtons();
            updateText();
        } catch (BedditConnectionException e) {
            //we could show a dialog or toast here
            this.finish();
        }

    }

    private void updateNightInfo() throws BedditConnectionException {
        //BedditWebConnector connectori = new BedditWebConnector();
        ApiController apiController = new ApiControllerClassImpl();

        apiController.updateSleepInfo(this);
        timeSleeping = apiController.getTimeSleeping(this);
        timeDeepSleep = apiController.getTimeDeepSleep(this);
        localAnalyzedUpToTime = apiController.getLocalAnalyzedUpToTime(this);
        isAnalysisUpToDate = apiController.getIsAnalysisUpToDate(this);
    }

    private void updateText() {
        ((TextView) findViewById(R.id.sleep_info_overall_text)).setText(getHoursAndMinutesFromSeconds(timeSleeping));
        ((TextView) findViewById(R.id.sleep_info_deep_text)).setText(getHoursAndMinutesFromSeconds(timeDeepSleep));

        Log.v(TAG, "Local analyzed up to time: " + localAnalyzedUpToTime + ", Device time is (LocaleString (+3gmt)) " + Calendar.getInstance().getTime().toLocaleString());
        if (isAnalysisUpToDate)
            ((TextView) findViewById(R.id.sleep_info_delay)).setText("Data is up to date");
        else
            ((TextView) findViewById(R.id.sleep_info_delay)).setText("Data is " + getTimeDifference(localAnalyzedUpToTime) + " old.");
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

    private String getHoursAndMinutesFromSeconds(int seconds) {
        return seconds / 3600 + "h " + (seconds / 60) % 60 + "min";
    }

    //expects format like this 2012-06-13T08:38:11 Please don't break it :)
    private String getTimeDifference(String data) {
        Calendar time = Utils.bedditTimeStringToCalendar(data);
        int differenceInSeconds = (int) ((Calendar.getInstance().getTimeInMillis() - time.getTimeInMillis())/1000);
        return getHoursAndMinutesFromSeconds(differenceInSeconds);
    }

    @Override
    public void onAttachedToWindow() {
        Log.v(TAG,"SETTING KEYGUARD ON");
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
