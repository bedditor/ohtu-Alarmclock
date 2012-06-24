package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.BedditException;
import java.util.Calendar;

public class SleepData {
    private String date;
    private int time_sleeping;
    private int time_deep_sleep;
    private String local_analyzed_up_to_time;
    private String[][] sleep_stages;
    private boolean is_analysis_up_to_date;

    public String getDate() {
        return date;
    }

    public char getLastSleepStage() throws BedditException {
        try {
            return sleep_stages[sleep_stages.length - 1][1].charAt(0);
        } catch (NullPointerException n) {
            throw new InvalidJsonException();
        }
    }

    public Calendar getLastSleepStageTime() throws InvalidJsonException {
        String timeString = sleep_stages[sleep_stages.length - 1][0];
        if(timeString == null)
            throw new InvalidJsonException("beddit says null");
        return TimeUtils.bedditTimeStringToCalendar(timeString);
    }

    public int getTimeSleeping() {
        return time_sleeping;
    }

    public int getTimeDeepSleep() {
        return time_deep_sleep;
    }

    public String getLocal_analyzed_up_to_time() throws InvalidJsonException {
        if(local_analyzed_up_to_time == null)
            throw new InvalidJsonException("beddit says null");
        return local_analyzed_up_to_time;
    }

    public boolean getIsAnalysisUpToDate() {
        return is_analysis_up_to_date;
    }
}
