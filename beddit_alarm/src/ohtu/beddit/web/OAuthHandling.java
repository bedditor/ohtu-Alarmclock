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
            scanscan.nextLine();
            token = scanscan.nextLine();
            token = token.substring(19,token.length()-2);
            Log.v("AccessToken" , token);
            //JsonParser lol = new JsonParser();
            //JsonElement hmm = lol.parse(token);
            //Log.v("TokenWat", hmm.getAsString());
        } catch (Throwable e) {
            Log.v("accesstoken", Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
        }
        return token;
    }

}
