package ohtu.beddit.web;

import android.content.Context;
import android.util.Log;
import ohtu.beddit.R;
import ohtu.beddit.io.PreferenceService;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class BedditConnectorImpl implements BedditConnector {
    @Override
    public String getUserJson(Context context){
        return getSomeJson(context, "");
    }

    @Override
    public String getWakeUpJson(Context context){
        return getSomeJson(context, "query"); //add
    }

    @Override
    public String getSomeJson(Context context, String query){
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        Scanner scanner = null;

        try {
            String token = PreferenceService.getToken(context);
            URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);

            System.setProperty("http.keepAlive", "false");
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
