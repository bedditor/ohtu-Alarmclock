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

public class BedditWebConnector {
    public String getUsernameJson(Context context){
        return getSomething(context, "");
    }

    public String getWakeUpJson(Context context){
        return getSomething(context, "query"); //add
    }

    public String getSomething(Context context, String query){
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        Scanner scanner = null;

        try {
            String token = PreferenceService.getSettingString(context, R.string.pref_key_userToken);
            URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            inputStream = connection.getInputStream();
            scanner = new Scanner(inputStream);
            while(scanner.hasNext())
                response += scanner.nextLine();
        }
        catch (Throwable e) {
            Log.e("LOL", Log.getStackTraceString(e));
        }
        finally {
            connection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        return response;
    }

}
