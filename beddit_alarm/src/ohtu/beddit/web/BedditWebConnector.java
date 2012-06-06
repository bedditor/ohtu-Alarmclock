package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
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
        try {
            String token = PreferenceService.getSettingString(context, R.string.pref_key_userToken);
            URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            Scanner scanner = new Scanner(new BufferedInputStream(connection.getInputStream()));
            while(scanner.hasNext())
                response += scanner.nextLine();
        } catch (Throwable e) {
            Log.e("LOL", Log.getStackTraceString(e));
        }

        return response;
    }

}
