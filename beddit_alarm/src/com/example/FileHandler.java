package com.example;

import android.content.*;

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
        String palautettava = "";
        FileInputStream fis = null;
        try{
            fis = context.openFileInput(filename);
        }catch(FileNotFoundException e){
            System.err.println("file not found");
            return "file not found";
        }
        try{
            int merkki = fis.read();
            if(merkki > -1){
                palautettava += (char)merkki;
            }
            while(merkki != -1){
                merkki=fis.read();
                if(merkki != -1){
                    palautettava += (char)merkki;
                }
            }
            fis.close();
        }catch (IOException f){
            System.err.println("Unable to read the file: "+filename);
            return "could not read from file";
        }

        return palautettava;
    }

    public static String getOAuth2Code(Context context){
        //not yet implemented
        return "";
    }

    public static String copyStr(String tocopy){
        String copy = "";
        for(int i = 0; i< tocopy.length(); i++){
            copy += tocopy.charAt(i);
        }
        return copy;
    }

    public static boolean saveAlarm(int hour, int minute, Context context){
        String towrite = ""+hour+'&'+minute+'\n';
        return writeToFile("alarms", towrite, context);
    }


    /*
        In the form of: min&hour\n
                        min&hour ...
     */
    public static String getAlarms(Context context){
        //pahasti kesken
        String alarmsString = readStringFromFile("alarms", context);
        char letter = 'a';
        String aux = "";
        String hour = "";
        String minute = "";
        for(int i = 0; i< alarmsString.length(); i++){
            letter = alarmsString.charAt(i);
            if(letter =='&'){
                hour = copyStr(aux);
                aux = "";
            }else if(letter == '\n'){
                minute = copyStr(aux);
                aux = "";
            }else{
                aux += letter;
            }
        }
        return "Hours: "+hour+" and minutes: "+minute;
    }

}
