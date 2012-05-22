package com.example;

import android.content.*;

import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 21.5.2012
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class FileHandler {

    public static boolean writeToFile(String filename, String writable){
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(filename);
        }catch(Exception e){
            System.err.println("herp derp");
            return false;
        }
        try{
            fos.write(writable.getBytes());
        }catch(Exception f){
            System.err.println("derp herp");
            return false;
        }
        return true;
    }

    public static String readStringFromFile(String filename){
        String palautettava = "";

        return palautettava;
    }

}
