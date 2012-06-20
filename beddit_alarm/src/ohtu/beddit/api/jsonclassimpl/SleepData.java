package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.utils.TimeUtils;
import ohtu.beddit.web.BedditException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 31.5.2012
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
class SleepData {
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

    public Calendar getLastSleepStageTime() {
        String timeString = sleep_stages[sleep_stages.length - 1][0];
        return TimeUtils.bedditTimeStringToCalendar(timeString);
    }

    public int getTimeSleeping() {
        return time_sleeping;
    }

    public int getTimeDeepSleep() {
        return time_deep_sleep;
    }

    public String getLocal_analyzed_up_to_time() {
        return local_analyzed_up_to_time;
    }

    public boolean getIsAnalysisUpToDate() {
        return is_analysis_up_to_date;
    }
}
