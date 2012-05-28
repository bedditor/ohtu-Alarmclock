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
        }catch(Exception e){
            System.err.println("Could not create fileoutputstream");
            return false;
        }
        try{
            fos.write(writable.getBytes());
            fos.close();
        }catch(IOException f){
            System.err.println("Could not write to file");
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
            System.err.println("file not found");
            return "";
        }
        try{
            int readCharacter = fis.read();
            if(readCharacter > -1){
                returnedString += (char)readCharacter;
            }
            while(readCharacter != -1){
                readCharacter=fis.read();
                if(readCharacter != -1){
                    returnedString += (char)readCharacter;
                }
            }
            fis.close();
        }catch (IOException f){
            System.err.println("Unable to read the file: "+filename);
            return "";
        }

        return returnedString;
    }

    public static String copyStr(String tocopy){
        String copy = "";
        for(int i = 0; i< tocopy.length(); i++){
            copy += tocopy.charAt(i);
        }
        return copy;
    }

    public static boolean saveAlarm(int hour, int minute, int interval, Context context, boolean isAlarm){
        int realAlarm = 0;
        if(isAlarm){
            realAlarm = 1;
        }
        String towrite = ""+realAlarm+'#'+hour+'&'+minute+'?'+interval+'\n';
        return writeToFile("alarms", towrite, context);
    }

    /*
        returns int[4] in the form of   1: if alarm exists
                                        2: hours
                                        3: minutes
                                        4: interval
     */
    public static int[] getAlarms(Context context){
        //pahasti kesken
        String alarmsString = readStringFromFile("alarms", context);
        if(alarmsString == ""){
            int[] returnable = new int[4];
            returnable[0] = -2;
            returnable[1] = -2;
            returnable[2] = -2;
            returnable[3] = -2;
            return returnable;
        }
        char letter = 'a';
        String aux = "";
        String isAlarm = "";
        String hour = "";
        String minute = "";
        String interval = "";
        for(int i = 0; i< alarmsString.length(); i++){
            letter = alarmsString.charAt(i);
            if(letter == '#'){
                isAlarm = copyStr(aux);
                aux = "";
            }
            else if(letter =='&'){
                hour = copyStr(aux);
                aux = "";
            }else if(letter == '?'){
                minute = copyStr(aux);
                aux = "";
            }else if(letter == '\n'){
                interval = copyStr(aux);
                aux = "";
            }else{
                aux += letter;
            }
        }
        int[] clockValues = new int[4];
        try{
            clockValues[0] = Integer.parseInt(isAlarm);
            clockValues[1] = Integer.parseInt(hour);
            clockValues[2] = Integer.parseInt(minute);
            clockValues[3] = Integer.parseInt(interval);
        }catch (Exception e){
            int[] returnable = new int[4];
            returnable[0] = -1;
            returnable[1] = -1;
            returnable[2] = -1;
            returnable[3] = -1;
            return returnable;
        }
        return clockValues;
        //return "Hours: "+hour+" and minutes: "+minute+" and interval: "+interval;
    }


    public static boolean saveOAuth2code(String code, Context context){
        return writeToFile("OAuth2", code, context);
    }

    public static String loadOAuth2code(Context context){
        return readStringFromFile("OAuth2",context);
    }



}
