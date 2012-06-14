package ohtu.beddit.alarm;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.api.ApiController;
import ohtu.beddit.api.jsonclassimpl.ApiControllerClassImpl;
import ohtu.beddit.web.BedditWebConnector;
import ohtu.beddit.web.MalformedBedditJsonException;

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
    public boolean wakeUpNow(Context context,char sleepstage) {

        int atMostMillisOld = 1000 * 60 * 5;
        ApiController api = new ApiControllerClassImpl(new BedditWebConnector());
        String dateString = getQueryDateString();
        try{
            api.updateSleepInfo(context,dateString);
            Calendar lastSleepTime = api.getTimeOfLastSleepStage();
            Calendar nao = Calendar.getInstance();
            long difference = nao.getTimeInMillis() - lastSleepTime.getTimeInMillis();
            Log.v("apidapi", "Time difference (minutes): "+difference/60/1000);
            if (difference > atMostMillisOld){ //jos uni-info liian vanhaa //derp, ei näin
                api.updateQueueInfo(context, dateString);
                String queueStatus = api.getSleepAnalysisStatus();
                if(queueStatus.equals("analysis_up_to_date")){ //jos ???
                    Log.v("apidapi", "up to date");
                }else if(queueStatus.equals("can_be_queued_for_analysis")){
                    api.requestInfoUpdate(context, dateString);
                    Log.v("apidapi", "update request");
                }else{
                    Log.v("apidapi", "was ist das");
                }
                return false;
            }else{ //eli on tarpeeksi uusi
                Log.v("apidapi", "sleepstage: "+api.getLastSleepStage());
                if(api.getLastSleepStage() == sleepstage){
                    return true;
                }
                return false;
            }
        }catch(MalformedBedditJsonException e){
            Log.v("apidapi", Log.getStackTraceString(e));
            Log.v("apidapi", "fug");
            return false;
        }
    }

    public static String getQueryDateString(){
        Calendar kalenteri = Calendar.getInstance();
        int year = kalenteri.get(Calendar.YEAR);
        int month = kalenteri.get(Calendar.MONTH);
        int day = kalenteri.get(Calendar.DAY_OF_MONTH);
        return year+"/"+(month+1)+"/"+day; //because the fucking months start from 0
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