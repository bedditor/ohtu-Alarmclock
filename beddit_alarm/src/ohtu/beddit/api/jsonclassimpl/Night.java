package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.utils.Utils;

import ohtu.beddit.web.MalformedBedditJsonException;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 31.5.2012
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
class Night extends JsonObject{
    private String date;
    private String[][] sleep_stages;

    public String getDate() {
        return date;
    }

    public char getLastSleepStage()throws MalformedBedditJsonException{
        try{
            return sleep_stages[sleep_stages.length-1][1].charAt(0);
        }catch(NullPointerException n){
            throw new MalformedBedditJsonException();
        }
    }

    public Calendar getLastSleepStageTime(){
        String timeString = sleep_stages[sleep_stages.length-1][0];
        return Utils.bedditTimeStringToCalendar(timeString);
    }


}
