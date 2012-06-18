package ohtu.beddit.io;

import android.content.*;
import android.util.Log;
import com.google.gson.JsonParser;
import ohtu.beddit.R;
import ohtu.beddit.alarm.Alarm;
import ohtu.beddit.utils.TimeUtils;

import java.io.*;
import java.util.Scanner;


public class FileHandler {

    public static final String ALARMS_FILENAME = "beddit_alarms";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    private Context context;

    public FileHandler(Context context){
        this.context = context.getApplicationContext();
    }

    public boolean writeToFile(String filename, String writable){
        FileOutputStream fos = null;
        try{
            fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
            fos.write(writable.getBytes());
            fos.close();
        }catch(Exception e){
            Log.v("Filehandler", "Writing crashed");
            return false;
        }
        return true;
    }

    public String readStringFromFile(String filename){
        try{
            Scanner scanner = new Scanner(context.openFileInput(filename));
            String line = scanner.nextLine();
            return line;
        }catch(Exception e){
            Log.v("Filehandler", "File not found");
            return "";
        }
    }

    public Alarm saveAlarm(int hours, int minutes, int interval, boolean enabled){
        int alarmSet = enabled ? 1 : -1;
        long millis = TimeUtils.timeToCalendar(hours, minutes).getTimeInMillis();
        String toWrite = ""+alarmSet+'#'+millis+'#'+interval;
        if(writeToFile(ALARMS_FILENAME, toWrite)){
            return new Alarm(hours, minutes, interval, enabled);
        }
        else{
            return saveDefaultAlarm();
        }
    }

    // Disables alarm, but keeps the wake up time in memory
    public void disableAlarm(){
        Alarm alarm = getAlarm();
        saveAlarm(alarm.getHours(), alarm.getMinutes(), alarm.getInterval(), false);
    }

    public Alarm getAlarm(){
        String[] alarmData = readStringFromFile(ALARMS_FILENAME).split("#");
        Alarm alarm = new Alarm();

        try{
            if(Integer.parseInt(alarmData[0]) > 0){
                alarm.setEnabled(true);
            }
            else alarm.setEnabled(false);
            alarm.setTimeInMillis(Long.parseLong(alarmData[1]));
            alarm.setInterval(Integer.parseInt(alarmData[2]));
        }
        catch (Exception e){ //Possible exceptions: parseInt fails, or if there was no alarm data, ArrayOutOfBoundsException
            Log.v("Exception", e.getMessage());
            saveDefaultAlarm();
        }
        return alarm;
    }

    private Alarm saveDefaultAlarm(){
        saveAlarm(8, 0, 0, false); //overwrite corrupted alarm data with default time 8:00 AM
        return new Alarm(8, 0, 0, false);
    }

    public String getClientInfo(String request) {
        String json= "";
        try{
            InputStream inputStream = context.getResources().openRawResource(R.raw.secret);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) { json+=scanner.next();}
        }catch(Exception e){
            Log.v("Filehandler", "File not found");
            return "";
        }
        String clientid=new JsonParser().parse(json).getAsJsonObject().get(request).getAsString();
        return clientid;
    }

}
