package ohtu.beddit.alarm;

import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.BedditWebConnector;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 5.6.2012
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class AlarmCheckerRealImpl implements AlarmChecker{

    @Override
    public boolean wakeUpNow(char sleepstage) {

        BedditApiController api = new BedditApiController(new BedditWebConnector());
        //check for sleepstage || ask for api if should wakeup
        return false;
    }

    public static String getQueryDateString(){
        Calendar kalenteri = Calendar.getInstance();
        int year = kalenteri.get(Calendar.YEAR);
        int month = kalenteri.get(Calendar.MONTH);
        int day = kalenteri.get(Calendar.DAY_OF_MONTH);
        return year+"/"+(month+1)+"/"+day;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 180;
    }

    @Override
    public int getCheckTime() {
        return 5;
    }
}