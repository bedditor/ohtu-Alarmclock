package ohtu.beddit.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 31.5.2012
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class Night {
    public String date;
    public String[][] sleep_stages;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public char getLastSleepStage(){
        return sleep_stages[sleep_stages.length-1][1].charAt(0);
    }

    public Calendar getTimeOfLastSleepStage(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        Date date;
        try {
            date = (Date)dateFormat.parse(sleep_stages[sleep_stages.length-1][0].replaceAll("T","-"));
        } catch (ParseException e) {
            System.out.println("Night: "+e.getMessage());
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public String[][] getSleep_stages() {
        return sleep_stages;
    }

    public void setSleep_stages(String[][] sleep_stages) {
        this.sleep_stages = sleep_stages;
    }

    /*{
           "date": "2012-03-14",
           "timezone": "Europe/Helsinki",
           "local_start_time": "2012-03-13T21:00:00",
           "local_end_time": "2012-03-14T10:00:00",
           "local_analyzed_up_to_time": "2012-03-14T09:59:52",
           "is_analysis_up_to_date": true, // Analysis is not lagging real-time
// There will be no more changes for this nights sleep information
           "analysis_complete": true,
// If this is false, sleep result fields are not included in response:
           "analysis_valid": true,
           "stress_percent": 73,
           "time_in_bed": 32292, // These summary times are seconds
           "time_light_sleep": 21064,
           "time_sleeping": 31406,
           "time_deep_sleep": 10342,
           "sleep_efficiency": 97.2562863867, // Percent of time in bed slept
           "resting_heartrate": 53.2544378696,
           "minutely_actigram": [
       0,
               2,
       …
// Rest of the actigram. An integer value for each minute
// between the night start time and night end time. Basically,
// it’s the number of movements occurred.
       ]
       "noise_measurements": [
       [
       [
       "2012-03-13T21:00:10",
               43.0 // Maximum noise value in dB
       ],
       … // Array of arrays of noise measurements
       ]
       ],
       "luminosity_measurements": [
       [
       [
       "2012-03-13T21:00:10",
               0.0
       ],
       … // Rest of the luminosity measurements in Lux
       ]
       ],
       "sleep_stages": [
       [
       "2012-03-13T21:00:00",
               "A" // Away
       ],
       [
       "2012-03-13T23:17:00",
               "W" // Wake
       ],
       [
       "2012-03-13T23:25:06",
               "L" // Light sleep
       ],
       [
       "2012-03-13T23:56:00",
               "D" // Deep sleep
       ],
       [
       "2012-03-13T23:58:00",
               "M" // Data is missing
       ],
       … // Rest of the sleep stages
   } */
}
