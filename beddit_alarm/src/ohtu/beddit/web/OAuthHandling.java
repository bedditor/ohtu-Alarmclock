package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OAuthHandling {

    public static String getAccessToken(Context context, String url){
        String token = "error";
        try {
            URL address = new URL(url);
            HttpsURLConnection connect = (HttpsURLConnection) address.openConnection();
            connect.connect();
            Scanner scanscan = new Scanner(connect.getInputStream());
            String stash = "";
            while(scanscan.hasNext()) {
                stash += scanscan.next();
            }
            connect.disconnect();
            String token1 = new JsonParser().parse(stash).getAsJsonObject().get("access_token").getAsString();
            Log.v("AccessToken" ,"AccessToken = \""+token1+"\"");

            token = token1;
        } catch (Throwable e) {
            Log.v("AccessToken", Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
        }
        return token;
    }

}
