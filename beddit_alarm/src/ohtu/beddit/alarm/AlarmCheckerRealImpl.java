package ohtu.beddit.alarm;

import android.util.Log;
import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.BedditWebConnector;

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
        BedditApiController api = new BedditApiController();
        BedditWebConnector web = new BedditWebConnector();
        //check for sleepstage || ask for api if should wakeup
        return false;
    }

    @Override
    public int getWakeUpAttemptInterval() {
        return 180;
    }

    @Override
    public int getCheckTime() {
        return 10;
    }
}