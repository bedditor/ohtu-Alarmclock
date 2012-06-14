package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.Scanner;

public class OAuthHandling {

    private static final String TAG = "OAuthHandling";

    public static String getAccessToken(Context context, String url){
        String token = "error";
        HttpsURLConnection connect = null;
        InputStream inputStream = null;
        Scanner scanscan = null;
        try {
            URL address = new URL(url);
            Log.v(TAG,"url: "+address);

            System.setProperty("http.keepAlive", "false");
            connect = (HttpsURLConnection) address.openConnection();

            connect.connect();
            Log.v(TAG,"responseCode: "+connect.getResponseCode());

            inputStream = connect.getInputStream();
            scanscan = new Scanner(inputStream);

            String stash = "";
            while(scanscan.hasNext()) {
                stash += scanscan.next();
            }
            Log.v(TAG,"stash: "+stash);

            String token1 = new JsonParser().parse(stash).getAsJsonObject().get("access_token").getAsString();
            Log.v(TAG,"AccessToken = \""+token1+"\"");

            token = token1;
        } catch (Throwable e) {
            Log.e(TAG, Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
            token = "error";
        }
        finally {
            connect.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    return "error";
                }
            }
            return token;
        }
    }

}
