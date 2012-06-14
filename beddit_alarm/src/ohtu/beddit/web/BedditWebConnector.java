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

    private static final String TAG = "BedditWebConnector";

    public String getUserJson(Context context) throws MalformedBedditJsonException {
        return getSomeJson(context, "", false);
    }


    public String getWakeUpJson(Context context,String date) throws MalformedBedditJsonException {
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep";
        return getSomeJson(context, query, false); //add
    }

    public String getQueueStateJson(Context context, String date) throws MalformedBedditJsonException{
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep/queue_for_analysis";
        return getSomeJson(context, query, false); //add
    }

    //need to check if POSTing actually works as it is:
    public String requestDataAnalysis(Context context, String date) throws MalformedBedditJsonException{
        String username = PreferenceService.getUsername(context);
        String query = username+"/"+date+"/sleep/queue_for_analysis";
        return getSomeJson(context, query, true); //add
    }

    public String getSomeJson(Context context, String query, boolean do_post) throws MalformedBedditJsonException {
        String response = "";

        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = connect(context, query, connection,do_post);
            inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNext())
                response += scanner.nextLine();
        }
        catch (Throwable e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new MalformedBedditJsonException();
        }
        finally {
            closeConnections(connection, inputStream);
            if(response.equals("")) {
                Log.v(TAG, "Empty response");
                throw new MalformedBedditJsonException();
            }
        }
        return response;
    }

    private HttpsURLConnection connect(Context context, String query, HttpsURLConnection connection, boolean do_post) throws IOException {
            String token = PreferenceService.getToken(context);
        URL url = new URL("https://api.beddit.com/api2/user/"+query+"?access_token="+token);
        Log.v(TAG, "GET Token: " + url);
        connection = (HttpsURLConnection) url.openConnection();
        if(do_post){
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        }else{
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
        }
        connection.connect();
        return connection;
    }

    private void closeConnections(HttpsURLConnection connection, InputStream inputStream) {
        if (connection != null){
            connection.disconnect();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }


}
