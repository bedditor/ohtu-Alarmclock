package ohtu.beddit.alarm;

import ohtu.beddit.json.BedditApiController;
import ohtu.beddit.web.BedditConnector;
import ohtu.beddit.web.BedditConnectorImpl;

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
        BedditApiController api = new BedditApiController(new BedditConnectorImpl());
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