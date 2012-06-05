package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ohtu.beddit.io.FileHandler;

import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 24.5.2012
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class OAuthHandling {

    public static String getAccessToken(Context context, String url){
        String token = "error";
        try {
            URL address = new URL(url);
            HttpsURLConnection connect = (HttpsURLConnection) address.openConnection();
            connect.connect();
            Scanner scanscan = new Scanner(connect.getInputStream());
            String stash = scanscan.nextLine();
            while(scanscan.hasNext()) {
                stash += scanscan.nextLine();
            }

            Log.v("AccessToken/Debug", "Parse phase 0.:\t" + stash);
            stash = stash.replace("\n", "");
            Log.v("AccessToken/Debug", "Parse phase 1.:\t" + stash);
            stash = stash.replace("\"", "");
            Log.v("AccessToken/Debug", "Parse phase 2.:\t" + stash);
            stash = stash.replace(":", "");
            Log.v("AccessToken/Debug", "Parse phase 3.:\t" + stash);
            stash = stash.replace("{", "");
            stash = stash.replace("}", "");
            Log.v("AccessToken/Debug", "Parse phase 4.:\t" + stash);
            stash = stash.trim();
            stash = stash.substring("access_token".length());
            stash = stash.trim();
            Log.v("AccessToken/Debug", "fullString 6.:\t" + stash);
            Log.v("AccessToken" ,"AccessToken = \""+stash+"\"");
            token = stash;
        } catch (Throwable e) {
            Log.v("AccessToken", Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
        }
        return token;
    }

}
