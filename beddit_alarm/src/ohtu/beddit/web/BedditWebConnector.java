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
    public String getUserJson(Context context) throws MalformedBedditJsonException {
        return getSomeJson(context, "");
    }

    public String getWakeUpJson(Context context) throws MalformedBedditJsonException {
        return getSomeJson(context, "query"); //add
    }

    public String getSomeJson(Context context, String query) throws MalformedBedditJsonException {
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = connect(context, query, connection);
            inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNext())
                response += scanner.nextLine();
        }
        catch (Throwable e) {
            Log.e("WebConnector", Log.getStackTraceString(e));
            throw new MalformedBedditJsonException();
        }
        finally {
            closeConnections(connection, inputStream);
            if(response.equals("")) throw new MalformedBedditJsonException();
        }
        return response;
    }

    private HttpsURLConnection connect(Context context, String query, HttpsURLConnection connection) throws IOException {
        String token = PreferenceService.getToken(context);
        URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
        Log.v("GET", "Token: " + url);
        connection = (HttpsURLConnection) url.openConnection();
        connection.connect();
        return connection;
    }

    private void closeConnections(HttpsURLConnection connection, InputStream inputStream) {
        connection.disconnect();
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("WebConnector", Log.getStackTraceString(e));
            }
        }
    }


}
