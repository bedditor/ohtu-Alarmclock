package ohtu.beddit.io;

import android.content.*;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class FileHandler {

    public static final String ALARMS_FILENAME = "beddit_alarms";


    public static boolean writeToFile(String filename, String writable, Context context){
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

    public static String readStringFromFile(String filename, Context context){
        try{
            Scanner scanner = new Scanner(context.openFileInput(filename));
            String line = scanner.nextLine();
            return line;
        }catch(Exception e){
            Log.v("Filehandler", "File not found");
            return "";
        }
    }

    public static boolean saveAlarm(int hour, int minute, int interval, boolean enabled, Context context){
        int alarmSet = enabled ? 1 : -1;
        String towrite = ""+alarmSet+'#'+hour+'#'+minute+'#'+interval;
        return writeToFile(ALARMS_FILENAME, towrite, context);
    }


    // Disables alarm, but keeps the wake up time in memory
    public static boolean disableAlarm(Context context){
        int[] oldData = getAlarm(context);
        return saveAlarm(oldData[1], oldData[2], oldData[3], false, context);
    }

    /*
        returns int[4] in the form of   0: if alarm exists
                                        1: hours
                                        2: minutes
                                        3: interval
     */
    public static int[] getAlarm(Context context){
        String[] alarmData = readStringFromFile(ALARMS_FILENAME, context).split("#");

        int[] alarmValues = new int[4];
        try{
            alarmValues[0] = Integer.parseInt(alarmData[0]);
            alarmValues[1] = Integer.parseInt(alarmData[1]);
            alarmValues[2] = Integer.parseInt(alarmData[2]);
            alarmValues[3] = Integer.parseInt(alarmData[3]);
        }catch (Exception e){
            Log.v("Exception", e.getMessage());
            //  Possible exceptions: parseInt fails, or if there was no alarm data, ArrayOutOfBoundsException
            alarmValues[0] = -1;
            for (int i = 1; i < alarmValues.length; i++){
                alarmValues[i] = 0;
            }
            saveAlarm(0, 0, 0, false, context);
        }
        return alarmValues;
    }

}
