package ohtu.beddit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ohtu.beddit.R;
import ohtu.beddit.api.jsonclassimpl.InvalidJsonException;
import ohtu.beddit.utils.DialogUtils;
import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditException;

import java.util.Calendar;


public class SleepInfoActivity extends Activity {

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
        } catch (BedditException e) {
            Log.v(TAG, e.getMessage());
            if(e instanceof BedditConnectionException){
                DialogUtils.createActivityClosingDialog(this, getString(R.string.could_not_connect), getString(R.string.button_text_ok));
            }
            else if(e instanceof InvalidJsonException){
                DialogUtils.createActivityClosingDialog(this, getString(R.string.sleep_data_fail), getString(R.string.button_text_ok));
            }
            else this.finish();
        }
    }

    private void updateNightInfo() throws BedditException {
        ApiController apiController = new ApiControllerClassImpl();
        if(apiController.isSleepInfoFuckingOld()){
            Log.v("sleepinfoupdate", "Is fucking old");
            apiController.updateSleepInfo(this);
        }
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
        ((Button) findViewById(R.id.SleptWellButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepInfoActivity.this.finish();
            }
        });
        ((Button) findViewById(R.id.SleptBadlyButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepInfoActivity.this.finish();
            }
        });
    }

    private String getHoursAndMinutesFromSeconds(int seconds) {
        return seconds / 3600 + "h " + (seconds / 60) % 60 + "min";
    }

    //expects format like this 2012-06-13T08:38:11 Please don't break it :)
    private String getTimeDifference(String data) {
        Calendar time = TimeUtils.bedditTimeStringToCalendar(data);
        int differenceInSeconds = (int) ((Calendar.getInstance().getTimeInMillis() - time.getTimeInMillis())/1000);
        return getHoursAndMinutesFromSeconds(differenceInSeconds);
    }
}
