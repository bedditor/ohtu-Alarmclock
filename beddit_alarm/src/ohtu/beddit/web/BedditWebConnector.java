package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class BedditWebConnector implements BedditConnector {
    public String getUserJson(Context context){
        return getSomeJson(context, "");
    }

    public String getWakeUpJson(Context context){
        return getSomeJson(context, "query"); //add
    }

    public String getSomeJson(Context context, String query){
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        Scanner scanner = null;

        try {
            String token = PreferenceService.getSettingString(context, R.string.pref_key_userToken);
            Log.v("GET","Token: "+token);
            URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
            Log.v("GET","Token: "+url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            scanner = new Scanner(inputStream);
            while(scanner.hasNext())
                response += scanner.nextLine();
        }
        catch (Throwable e) {
            Log.e("LOL", Log.getStackTraceString(e));
            response = "";
        }
        finally {
            connection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return "";
                }
            }
            return response;
        }
    }

}
