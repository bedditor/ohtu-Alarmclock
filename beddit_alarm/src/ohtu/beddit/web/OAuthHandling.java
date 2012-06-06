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
import java.util.Scanner;

public class OAuthHandling {

    public static String getAccessToken(Context context, String url){
        String token = "error";
        HttpsURLConnection connect = null;
        InputStream inputStream = null;
        Scanner scanscan = null;
        try {
            URL address = new URL(url);
            Log.v("OAuth","url: "+address);

            System.setProperty("http.keepAlive", "false");
            connect = (HttpsURLConnection) address.openConnection();
            connect.connect();
            Log.v("OAuth","responseCode: "+connect.getResponseCode());


            inputStream = connect.getInputStream();
            scanscan = new Scanner(inputStream);

            String stash = "";
            while(scanscan.hasNext()) {
                stash += scanscan.next();
            }
            Log.v("OAuth","stash: "+stash);
            String token1 = new JsonParser().parse(stash).getAsJsonObject().get("access_token").getAsString();
            Log.v("AccessToken" ,"AccessToken = \""+token1+"\"");

            token = token1;
        } catch (Throwable e) {
            Log.v("AccessToken", Log.getStackTraceString(e));  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            connect.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            scanscan.close();
        }
        return token;
    }

}
