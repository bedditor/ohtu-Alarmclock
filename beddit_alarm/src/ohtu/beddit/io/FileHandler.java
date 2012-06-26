package ohtu.beddit.io;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonParser;
import ohtu.beddit.R;
import ohtu.beddit.alarm.Alarm;
import ohtu.beddit.alarm.AlarmReceiver;
import ohtu.beddit.alarm.NotificationFactory;
import ohtu.beddit.utils.TimeUtils;

import java.io.*;
import java.util.Scanner;

/*
 * This class handles the file I/O. Files are used for saving the client info (Application id and secret) and
 * alarm. If the application is ever updated to support multiple alarms, it is recommended to save alarms
 * into a database instead of a file.
 */
public class FileHandler {

    private static final String TAG = "FileHandler";

    private static final String ALARMS_FILENAME = "beddit_alarms";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRET = "client_secret";
    private final Context context;

    public FileHandler(Context context) {
        this.context = context.getApplicationContext();

        try {
            FileInputStream inputStream = context.openFileInput(ALARMS_FILENAME);
        } catch (FileNotFoundException e) {
            saveDefaultAlarm();
        }
    }

    private void saveDefaultAlarm() {
        saveAlarm(8, 0, 15, false);
    }



    /**
     * Saves the given alarm to file.
     * @param hours alarm time hours (0-23)
     * @param minutes alarm time minutes (0-59)
     * @param interval alarm interval as minutes
     * @param enabled is alarm enabled
     * @return alarm as an object
     */
    public Alarm saveAlarm(int hours, int minutes, int interval, boolean enabled) {
        boolean success = true;
        Alarm alarm = new Alarm(hours, minutes, interval, enabled);

        ObjectOutput output = null;
        try {
            output = new ObjectOutputStream(context.openFileOutput(ALARMS_FILENAME, Context.MODE_PRIVATE));
            output.writeObject(alarm);
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        } finally {
            if(output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
            }
        }

        if(!success){
            explode();
        }

        return alarm;
    }



    /**
     * Disables alarm, but keeps the wake up time saved in file
     */
    public void disableAlarm() {
        Alarm alarm = getAlarm();
        saveAlarm(alarm.getHours(), alarm.getMinutes(), alarm.getInterval(), false);
    }


    /**
     * Reads alarm from file and returns it as an object
     * @return saved alarm data
     */
    public Alarm getAlarm()  {
        Alarm alarm = null;
        ObjectInput input = null;
        boolean success = true;

        try {
            input = new ObjectInputStream(context.openFileInput(ALARMS_FILENAME));
            alarm = (Alarm) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
            }
        }

        if(!success){
            explode();
        }

        return alarm;
    }


    /**
     * Reads client id/secret from file.
     * @param request data to be read
     * @return client id/secret
     */
    public String getClientInfo(String request) {
        String json = "";
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.secret);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNext()) {
                json += scanner.next();
            }
        } catch (Exception e) {
            Log.v(TAG, "File not found");
            return "";
        }
        return new JsonParser().parse(json).getAsJsonObject().get(request).getAsString();
    }

    private void explode(){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        context.deleteFile(ALARMS_FILENAME);
        new NotificationFactory(context).resetNotification();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
