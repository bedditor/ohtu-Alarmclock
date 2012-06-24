package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.utils.TimeUtils;
import java.util.Calendar;

public class QueueData {
    private String results_available_up_to;
    private String sleep_analysis_status;
    private String results_available_up_to_when_sleep_analyzed;
    private String results_available_up_to_when_queued_for_sleep_analysis;

    public String getSleepAnalysisStatus() {
        return sleep_analysis_status;
    }

    public Calendar getResults_available_up_to() throws InvalidJsonException { //for tests
        if(results_available_up_to == null)
            throw new InvalidJsonException("beddit says null");
        return TimeUtils.bedditTimeStringToCalendar(results_available_up_to);
    }

    public Calendar getWhenSleepAnalyzed() throws InvalidJsonException {
        if(results_available_up_to_when_sleep_analyzed == null)
            throw new InvalidJsonException("beddit says null");
        return TimeUtils.bedditTimeStringToCalendar(results_available_up_to_when_sleep_analyzed);
    }

    public Calendar getWhen_queued_for_sleep_analysis() { //can be null, for tests
        return TimeUtils.bedditTimeStringToCalendar(results_available_up_to_when_queued_for_sleep_analysis);
    }
}
