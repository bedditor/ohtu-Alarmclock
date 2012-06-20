package ohtu.beddit.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import ohtu.beddit.R;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.api.jsonclassimpl.InvalidJsonException;
import ohtu.beddit.utils.DialogUtils;
import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.BedditConnectionException;
import ohtu.beddit.web.BedditException;
import ohtu.beddit.web.LoadingDialog;

import java.util.Calendar;


public class SleepInfoActivity extends Activity {

    private int timeSleeping;
    private int timeDeepSleep;
    private String localAnalyzedUpToTime;
    private boolean isAnalysisUpToDate;
    private final String TAG = "SleepInfoActivity";
    private LoadingDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_info);

        dialog = new LoadingDialog(this);
        dialog.show();

        setEventHandlers();
        new SleepInfoLoader().execute();
    }

    private class SleepInfoLoader extends AsyncTask<Void, Void, Integer> {
        private static final int RESULT_SUCCESS = 1;
        private static final int RESULT_CONNECTION_FAIL = 2;
        private static final int RESULT_BAD_DATA = 3;
        private static final int RESULT_FAILURE = 4;

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                updateNightInfo();
            } catch (BedditException e) {
                Log.v(TAG, e.getMessage());
                if (e instanceof BedditConnectionException)
                    return RESULT_CONNECTION_FAIL;
                else if (e instanceof InvalidJsonException)
                    return RESULT_BAD_DATA;
                else
                    return RESULT_FAILURE;
            }
            return RESULT_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer resultCode) {
            switch (resultCode) {
                case (RESULT_SUCCESS):
                    updateText();
                    break;
                case (RESULT_BAD_DATA):
                    DialogUtils.createActivityClosingDialog(SleepInfoActivity.this, getString(R.string.sleep_data_fail), getString(R.string.button_text_ok));
                    break;
                case (RESULT_CONNECTION_FAIL):
                    DialogUtils.createActivityClosingDialog(SleepInfoActivity.this, getString(R.string.could_not_connect), getString(R.string.button_text_ok));
                    break;
                case (RESULT_FAILURE):
                    Log.v(TAG, "Unknown failure loading sleep information");
                    SleepInfoActivity.this.finish();
                    break;
            }
            SleepInfoActivity.this.dialog.dismiss();
        }
    }

    private void updateNightInfo() throws BedditException {
        ApiController apiController = new ApiControllerClassImpl();
        if (apiController.isSleepInfoOutdated() || apiController.hasUserChanged(this)) {
            Log.v(TAG, "Sleep info outdated");
            apiController.updateSleepData(this);
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
            ((TextView) findViewById(R.id.sleep_info_delay)).setText(this.getString(R.string.sleep_info_up_to_date));
        else
            ((TextView) findViewById(R.id.sleep_info_delay)).setText(this.getString(R.string.sleep_info_overall_age_pref) + " " + getTimeDifference(localAnalyzedUpToTime) + " " + this.getString(R.string.sleep_info_overall_age_post));
    }

    private void setEventHandlers() {
        findViewById(R.id.SleptWellButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepInfoActivity.this.finish();
            }
        });
        findViewById(R.id.SleptBadlyButton).setOnClickListener(new View.OnClickListener() {
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
        int differenceInSeconds = (int) ((Calendar.getInstance().getTimeInMillis() - time.getTimeInMillis()) / 1000);
        return getHoursAndMinutesFromSeconds(differenceInSeconds);
    }
}
