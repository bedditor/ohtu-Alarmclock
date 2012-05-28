package ohtu.beddit.io;

import android.content.*;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 21.5.2012
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class FileHandler {

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
        String returnedString = "";
        FileInputStream fis = null;
        try{
            fis = context.openFileInput(filename);
        }catch(FileNotFoundException e){
            Log.v("Filehandler", "File not found");
            return "";
        }
        try{
            int readCharacter = 0;
            while(readCharacter != -1){
                readCharacter=fis.read();
                if(readCharacter != -1){
                    returnedString += (char)readCharacter;
                }
            }
            fis.close();
        }catch (IOException f){
            Log.v("Filehandler", "Can't read file: "+filename);
            return "";
        }

        return returnedString;
    }

    public static String copyStr(String tocopy){
        return new String(tocopy);
    }


    public static boolean saveAlarm(int hour, int minute, int interval, Context context, boolean isAlarm){
        int realAlarm = 0;
        if(isAlarm){
            realAlarm = 1;
        }
        String towrite = ""+realAlarm+'#'+hour+'#'+minute+'#'+interval;
        return writeToFile("alarms", towrite, context);
    }

    /*
        returns int[4] in the form of   1: if alarm exists
                                        2: hours
                                        3: minutes
                                        4: interval
     */
    public static int[] getAlarms(Context context){
        String[] alarmData = readStringFromFile("alarms", context).split("#");

        int[] alarmValues = new int[4];
        try{
            alarmValues[0] = Integer.parseInt(alarmData[0]);
            alarmValues[1] = Integer.parseInt(alarmData[1]);
            alarmValues[2] = Integer.parseInt(alarmData[2]);
            alarmValues[3] = Integer.parseInt(alarmData[3]);
        }catch (Exception e){
            Log.v("Exception", e.getMessage());
            //  Possible exceptions: parseInt fails, or if there was no alarm data, ArrayOutOfBoundsException
            for (int i = 0; i < alarmValues.length; i++){
                alarmValues[i] = -1;
            }
        }
        return alarmValues;
    }


    public static boolean saveOAuth2code(String code, Context context){
        return writeToFile("OAuth2", code, context);
    }

    public static String loadOAuth2code(Context context){
        return readStringFromFile("OAuth2",context);
    }

}
