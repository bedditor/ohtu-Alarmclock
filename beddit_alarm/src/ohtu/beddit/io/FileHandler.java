package ohtu.beddit.io;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonParser;
import ohtu.beddit.R;
import ohtu.beddit.alarm.Alarm;
import ohtu.beddit.utils.TimeUtils;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
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
    }

    private boolean writeToFile(String filename, String writable) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(writable.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.v(TAG, "Writing crashed");
            return false;
        }
        return true;
    }

    private String readStringFromFile(String filename) {
        try {
            Scanner scanner = new Scanner(context.openFileInput(filename));
            return scanner.nextLine();
        } catch (Exception e) {
            Log.v(TAG, "File not found");
            return "";
        }
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
        int alarmSet = enabled ? 1 : -1;
        long millis = TimeUtils.timeToCalendar(hours, minutes).getTimeInMillis();
        String toWrite = "" + alarmSet + '#' + millis + '#' + interval;
        if (writeToFile(ALARMS_FILENAME, toWrite)) {
            return new Alarm(hours, minutes, interval, enabled);
        } else {
            return saveDefaultAlarm();
        }
    }

    /**
     * Disables the previous alarm but keeps it saved in a file.
     */
    public void disableAlarm() {
        Alarm alarm = getAlarm();
        saveAlarm(alarm.getHours(), alarm.getMinutes(), alarm.getInterval(), false);
    }

    /**
     * Reads alarm from file and returns it as an object.
     * If there's an Exception reading the alarm, return a default alarm of 8 am.
     * @return saved alarm data
     */
    public Alarm getAlarm() {
        String[] alarmData = readStringFromFile(ALARMS_FILENAME).split("#");
        Alarm alarm = new Alarm();

        try {
            if (Integer.parseInt(alarmData[0]) > 0) {
                alarm.setEnabled(true);
            } else alarm.setEnabled(false);
            alarm.setTimeInMillis(Long.parseLong(alarmData[1]));
            alarm.setInterval(Integer.parseInt(alarmData[2]));
        } catch (Exception e) { //Possible exceptions: parseInt fails, or if there was no alarm data, ArrayOutOfBoundsException
            Log.v(TAG, e.getMessage());
            return saveDefaultAlarm();
        }
        return alarm;
    }

    /**
     * initializes file with an default disabled alarm and return it's object representation
     */
    private Alarm saveDefaultAlarm() {
        return saveAlarm(8, 0, 0, false); //overwrite corrupted alarm data with default time 8:00 AM
        //return new Alarm(8, 0, 0, false);
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

}
