package com.example;

/**
 * Created with IntelliJ IDEA.
 * User: juho
 * Date: 22.5.2012
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
